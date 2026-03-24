package com.beki.appointment.config;

import io.github.bucket4j.distributed.proxy.ProxyManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class RedisProxyManager implements ProxyManager<String> {

    private final RedisTemplate<String, Object> redisTemplate;
    
    private static final String SCRIPT = 
        "local key = KEYS[1]\n" +
        "local now = tonumber(ARGV[1])\n" +
        "local ttl = tonumber(ARGV[2])\n" +
        "local tokens = tonumber(ARGV[3])\n" +
        "\n" +
        "local bucket = redis.call('HMGET', key, 'tokens', 'last_refill')\n" +
        "local current_tokens = tonumber(bucket[1]) or tokens\n" +
        "local last_refill = tonumber(bucket[2]) or now\n" +
        "\n" +
        "if now - last_refill >= ttl then\n" +
        "    current_tokens = tokens\n" +
        "    last_refill = now\n" +
        "end\n" +
        "\n" +
        "if current_tokens > 0 then\n" +
        "    current_tokens = current_tokens - 1\n" +
        "    redis.call('HMSET', key, 'tokens', current_tokens, 'last_refill', last_refill)\n" +
        "    redis.call('EXPIRE', key, ttl)\n" +
        "    return 1\n" +
        "else\n" +
        "    redis.call('HMSET', key, 'tokens', current_tokens, 'last_refill', last_refill)\n" +
        "    redis.call('EXPIRE', key, ttl)\n" +
        "    return 0\n" +
        "end";

    public RedisProxyManager(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public <T> T getProxy(String key, Class<T> clazz) {
        return clazz.cast(new RedisBucket(key));
    }

    private class RedisBucket {
        private final String key;

        public RedisBucket(String key) {
            this.key = "rate_limit:" + key;
        }

        public boolean tryConsume(int tokens) {
            RedisScript<Long> script = new DefaultRedisScript<>(SCRIPT, Long.class);
            Long result = redisTemplate.execute(script, 
                Collections.singletonList(key), 
                String.valueOf(System.currentTimeMillis()),
                String.valueOf(Duration.ofMinutes(1).getSeconds()),
                String.valueOf(tokens));
            return result != null && result == 1;
        }
    }
}

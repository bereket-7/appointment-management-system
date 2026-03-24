package com.beki.appointment.config;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.time.LocalDateTime;

@Configuration
public class HealthCheckConfig {

    @Bean
    public HealthIndicator databaseHealthIndicator(DataSource dataSource) {
        return new DatabaseHealthIndicator(dataSource);
    }

    @Bean
    public HealthIndicator redisHealthIndicator(RedisConnectionFactory redisConnectionFactory) {
        return new RedisHealthIndicator(redisConnectionFactory);
    }

    @Bean
    public HealthIndicator applicationHealthIndicator() {
        return new ApplicationHealthIndicator();
    }

    private static class DatabaseHealthIndicator implements HealthIndicator {
        private final DataSource dataSource;

        public DatabaseHealthIndicator(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        @Override
        public Health health() {
            try {
                JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
                jdbcTemplate.queryForObject("SELECT 1", Integer.class);
                return Health.up()
                        .withDetail("database", "MySQL")
                        .withDetail("status", "Available")
                        .withDetail("timestamp", LocalDateTime.now())
                        .build();
            } catch (Exception e) {
                return Health.down()
                        .withDetail("database", "MySQL")
                        .withDetail("error", e.getMessage())
                        .withDetail("timestamp", LocalDateTime.now())
                        .build();
            }
        }
    }

    private static class RedisHealthIndicator implements HealthIndicator {
        private final RedisConnectionFactory redisConnectionFactory;

        public RedisHealthIndicator(RedisConnectionFactory redisConnectionFactory) {
            this.redisConnectionFactory = redisConnectionFactory;
        }

        @Override
        public Health health() {
            try {
                redisConnectionFactory.getConnection().ping();
                return Health.up()
                        .withDetail("redis", "Redis")
                        .withDetail("status", "Available")
                        .withDetail("timestamp", LocalDateTime.now())
                        .build();
            } catch (Exception e) {
                return Health.down()
                        .withDetail("redis", "Redis")
                        .withDetail("error", e.getMessage())
                        .withDetail("timestamp", LocalDateTime.now())
                        .build();
            }
        }
    }

    private static class ApplicationHealthIndicator implements HealthIndicator {
        @Override
        public Health health() {
            Runtime runtime = Runtime.getRuntime();
            
            return Health.up()
                    .withDetail("application", "Appointment Management System")
                    .withDetail("version", "1.0.0")
                    .withDetail("status", "Running")
                    .withDetail("timestamp", LocalDateTime.now())
                    .withDetail("java.version", System.getProperty("java.version"))
                    .withDetail("memory.used", formatBytes(runtime.totalMemory() - runtime.freeMemory()))
                    .withDetail("memory.free", formatBytes(runtime.freeMemory()))
                    .withDetail("memory.total", formatBytes(runtime.totalMemory()))
                    .withDetail("memory.max", formatBytes(runtime.maxMemory()))
                    .withDetail("processors", runtime.availableProcessors())
                    .build();
        }

        private String formatBytes(long bytes) {
            String[] units = {"B", "KB", "MB", "GB"};
            int unitIndex = 0;
            double size = bytes;

            while (size >= 1024 && unitIndex < units.length - 1) {
                size /= 1024;
                unitIndex++;
            }

            return String.format("%.2f %s", size, units[unitIndex]);
        }
    }
}

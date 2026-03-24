package com.beki.appointment.config;

import brave.Tracer;
import brave.handler.SpanHandler;
import brave.sampler.Sampler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.autoconfigure.tracing.ConditionalOnEnabledTracing;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnEnabledTracing
@Slf4j
public class TracingConfig {

    @Bean
    public Sampler alwaysSampler() {
        return Sampler.ALWAYS_SAMPLE;
    }

    @Bean
    public SpanHandler loggingSpanHandler() {
        return new SpanHandler() {
            @Override
            public boolean end(TraceContext context, Span span) {
                log.info("Span completed: {} - {} - {}ms", 
                        span.name(), 
                        span.kind(), 
                        span.finishTimestamp() - span.startTimestamp());
                return true;
            }
        };
    }

    @Bean
    public TracingService tracingService(Tracer tracer) {
        return new TracingService(tracer);
    }
}

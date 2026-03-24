package com.beki.appointment.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MetricsConfig {

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config().commonTags("application", "appointment-management");
    }

    @Bean
    public MetricsFilter metricsFilter(MeterRegistry meterRegistry) {
        return new MetricsFilter(meterRegistry);
    }

    public static class MetricsFilter extends OncePerRequestFilter {
        private final MeterRegistry meterRegistry;
        private final Timer.Sample sample;

        public MetricsFilter(MeterRegistry meterRegistry) {
            this.meterRegistry = meterRegistry;
            this.sample = Timer.start(meterRegistry);
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {
            
            String uri = request.getRequestURI();
            String method = request.getMethod();
            
            Timer.Sample sample = Timer.start(meterRegistry);
            
            try {
                filterChain.doFilter(request, response);
            } finally {
                sample.stop(Timer.builder("http.server.requests")
                        .tag("method", method)
                        .tag("uri", uri)
                        .tag("status", String.valueOf(response.getStatus()))
                        .register(meterRegistry));
                
                // Counter for active users
                if (uri.contains("/auth/login") && response.getStatus() == 200) {
                    meterRegistry.counter("user.login.success").increment();
                } else if (uri.contains("/auth/login") && response.getStatus() == 401) {
                    meterRegistry.counter("user.login.failure").increment();
                }
                
                // Counter for user registrations
                if (uri.contains("/users/register") && response.getStatus() == 201) {
                    meterRegistry.counter("user.registration.success").increment();
                }
                
                // Counter for appointments
                if (uri.contains("/appointments") && method.equals("POST") && response.getStatus() == 201) {
                    meterRegistry.counter("appointment.created").increment();
                } else if (uri.contains("/appointments") && method.equals("GET") && response.getStatus() == 200) {
                    meterRegistry.counter("appointment.retrieved").increment();
                }
            }
        }
    }
}

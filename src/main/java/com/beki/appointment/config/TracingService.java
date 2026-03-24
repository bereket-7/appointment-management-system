package com.beki.appointment.config;

import brave.Span;
import brave.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@Slf4j
public class TracingService {

    private final Tracer tracer;

    public <T> T traceOperation(String operationName, Supplier<T> operation) {
        Span span = tracer.nextSpan().name(operationName).start();
        
        try (Tracer.SpanInScope ws = tracer.withSpanInScope(span)) {
            log.debug("Starting operation: {}", operationName);
            T result = operation.get();
            log.debug("Completed operation: {}", operationName);
            return result;
        } catch (Exception e) {
            span.tag("error", e.getMessage());
            log.error("Error in operation {}: {}", operationName, e.getMessage());
            throw e;
        } finally {
            span.finish();
        }
    }

    public void traceOperation(String operationName, Runnable operation) {
        Span span = tracer.nextSpan().name(operationName).start();
        
        try (Tracer.SpanInScope ws = tracer.withSpanInScope(span)) {
            log.debug("Starting operation: {}", operationName);
            operation.run();
            log.debug("Completed operation: {}", operationName);
        } catch (Exception e) {
            span.tag("error", e.getMessage());
            log.error("Error in operation {}: {}", operationName, e.getMessage());
            throw e;
        } finally {
            span.finish();
        }
    }

    public void addTag(String key, String value) {
        Span currentSpan = tracer.currentSpan();
        if (currentSpan != null) {
            currentSpan.tag(key, value);
        }
    }

    public void addAnnotation(String annotation) {
        Span currentSpan = tracer.currentSpan();
        if (currentSpan != null) {
            currentSpan.annotate(annotation);
        }
    }
}

package me.mikael.mmobstest.events;

import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@RequiredArgsConstructor
public class MyEventPublisher {

    private final Tracer tracer;

    private final AtomicInteger counter = new AtomicInteger(0);
    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishEvent(String message) {
        log.info("Publishing custom event. ");


        var myEvent = new MyEvent(
                counter.incrementAndGet(),
                message,
                tracer.currentSpan().context().traceId(),
                tracer.currentSpan().context().spanId(),
                Map.of("custom-1", "value-1", "custom-2", "value-2")
        );
        applicationEventPublisher.publishEvent(myEvent);
    }
}

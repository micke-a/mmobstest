package me.mikael.mmobstest.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.mikael.mmobstest.support.TracingSupport;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MyEventConsumer{

    private final TracingSupport tracingSupport;

    @EventListener
    public void handleMyEvent(MyEvent event) {
        tracingSupport.withNewTrace(event.traceId(), event.spanId(), event.metadata(), () -> {
            log.info("In event consumer - handling event. message: {}", event.message());
        });
    }
}

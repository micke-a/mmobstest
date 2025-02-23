package me.mikael.mmobstest.controllers;

import io.micrometer.observation.NullObservation;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.mikael.mmobstest.service.MessageService;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {

    private final Tracer tracer;
    private final MessageService messageService;
    private final ObservationRegistry observationRegistry;

    @RequestMapping("/send")
    public String send(@RequestHeader(value = "Event-Id", required = false) String eventId) {

        log.info("In message/send controller method");
        var newTraceId = Optional.ofNullable(eventId).orElse(UUID.randomUUID().toString());
        new NullObservation(observationRegistry)
                .contextualName("custom.trace.id.from.request")

                .start()
                .observe(() -> {
                    Observation.start("custom.trace.id.from.request", observationRegistry)
                            .observe(() -> {
                                var currentSpan = tracer.currentSpan();
                                log.info("Current span: {}", currentSpan);

                                var scopedSpan = tracer.startScopedSpan("send.message.service.call");
                                messageService.sendMessage("Hello world!");
                                scopedSpan.end();
                            });
                });
        log.info("message sending finished");

        return "Message sent";
    }
}

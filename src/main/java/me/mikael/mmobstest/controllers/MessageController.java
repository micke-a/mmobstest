package me.mikael.mmobstest.controllers;

import io.micrometer.observation.NullObservation;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.mikael.mmobstest.events.MyEventPublisher;
import me.mikael.mmobstest.service.MessageService;
import me.mikael.mmobstest.support.TracingSupport;
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

    private final TracingSupport tracingSupport;
    private final MessageService messageService;


    @RequestMapping("/send")
    public String send(@RequestHeader(value = "Event-Id", required = false) String eventId) {

        log.info("In message/send controller method - creating new span with injected Tracer");
        tracingSupport.withNewSpan("send.message.service.call", () -> {
            log.info("Sending message");
            messageService.sendMessage("Hello world!");
        });

        log.info("Message sent");
        return "Message sent";
    }
}

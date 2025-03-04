package me.mikael.mmobstest.service;

import io.micrometer.tracing.TraceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.mikael.mmobstest.events.MyEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {
    private final MyEventPublisher myEventPublisher;

    public void sendMessage(String message){
        log.info("Sending message: {}", message);

        myEventPublisher.publishEvent(message);
        log.info("event published");
    }
}

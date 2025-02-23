package me.mikael.mmobstest.service;

import io.micrometer.tracing.TraceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageService {

    public void sendMessage(String message){
        log.info("Sending message: {}", message);

    }
}

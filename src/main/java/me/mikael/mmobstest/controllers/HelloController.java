package me.mikael.mmobstest.controllers;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.mikael.mmobstest.service.HelloService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/hello")
@RequiredArgsConstructor
public class HelloController {

    private final ObservationRegistry observationRegistry;
    private final HelloService helloService;
    @GetMapping("/world")
    public String helloWorld() {
        log.info("Hello there : )");

        var parent = Observation.createNotStarted("hello.service.calls", observationRegistry)
                .contextualName("manual_observation")
                .lowCardinalityKeyValue("service", "hello-service");
        parent.observe(() -> {
            log.info("In observe lambda");
            helloService.doStuff1();
            helloService.doStuff2();
            helloService.doStuff3();
        });

        return "Hello, World!";
    }
}

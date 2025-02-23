package me.mikael.mmobstest.service;

import io.micrometer.observation.annotation.Observed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HelloService {

    @Observed(name="doStuff1", contextualName = "doing stuff 1")
    public void doStuff1() {
        log.info("Doing stuff 1 in HelloService");
    }

    @Observed(name="doStuff2", contextualName = "doing stuff 2")
    public void doStuff2() {
        log.info("Doing stuff 2 in HelloService");
    }

    public void doStuff3() {
        log.info("Doing stuff 3 in HelloService");
    }
}

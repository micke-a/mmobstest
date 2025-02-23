
Playground to see what is new with micrometer changes.


Some questions of mine online on this topic:
- https://stackoverflow.com/questions/79402301/spring-boot-3-with-micrometer-1-4-how-to-skip-tracing-but-continue-to-report-m
- https://micrometer-metrics.slack.com/archives/C662HUJC9/p1740167018704599


## Links

Localhost Kibana: 
- http://localhost:5601/app/home#/

Localhost Zipkin: 
- http://localhost:9411/zipkin/

Localhost Prometheus: 
- http://localhost:9090/query?g0.expr=http_server_requests_seconds_count

Spring boot stuff
- https://spring.io/blog/2022/10/12/observability-with-spring-boot-3

micrometer stuff
- https://docs.micrometer.io/tracing/reference/api.html

## Docker compose  links

ELK stack
https://medium.com/@lahiruchandika/integrate-elk-stack-into-spring-boot-application-4991e7918c3a

Prometheus
https://stackoverflow.com/questions/77365532/local-prometheus-cant-reach-spring-boot-app-running-in-ide

## What happens when using Observations?

Tested using HelloController and HelloService.

Controller with manually created observation 
```java

var parent = Observation.createNotStarted("hello.service.calls", observationRegistry)
    .contextualName("manual_observation")
    .lowCardinalityKeyValue("service", "hello-service");
parent.observe(() -> {
    log.info("In observe lambda");
    helloService.doStuff1();
    helloService.doStuff2();
    helloService.doStuff3();
});
```

Calling a service with @Observed method
```java
@Slf4j
@Service
public class HelloService {

    @Observed(name="doStuff")
    public void doStuff() {
        log.info("Doing stuff in HelloService");
    }
}
```

We get these metrics in Prometheus
```
hello_service_calls_active_seconds_count{application="app", instance="host.docker.internal:8080", job="backend", service="hello-service"}	0
hello_service_calls_active_seconds_sum{application="app", instance="host.docker.internal:8080", job="backend", service="hello-service"}	0
hello_service_calls_active_seconds_max{application="app", instance="host.docker.internal:8080", job="backend", service="hello-service"}	0
hello_service_calls_seconds_count{application="app", error="none", instance="host.docker.internal:8080", job="backend", service="hello-service"}	1
hello_service_calls_seconds_sum{application="app", error="none", instance="host.docker.internal:8080", job="backend", service="hello-service"}	0.0135293
hello_service_calls_seconds_max{application="app", error="none", instance="host.docker.internal:8080", job="backend", service="hello-service"}	0.0135293

doStuff1_active_seconds_count{application="app", class="me.mikael.mmobstest.service.HelloService", instance="host.docker.internal:8080", job="backend", method="doStuff1"}	0
doStuff1_active_seconds_sum{application="app", class="me.mikael.mmobstest.service.HelloService", instance="host.docker.internal:8080", job="backend", method="doStuff1"}	0
doStuff1_active_seconds_max{application="app", class="me.mikael.mmobstest.service.HelloService", instance="host.docker.internal:8080", job="backend", method="doStuff1"}	0
doStuff1_seconds_count{application="app", class="me.mikael.mmobstest.service.HelloService", error="none", instance="host.docker.internal:8080", job="backend", method="doStuff1"}	1
doStuff1_seconds_sum{application="app", class="me.mikael.mmobstest.service.HelloService", error="none", instance="host.docker.internal:8080", job="backend", method="doStuff1"}	0.0005765
doStuff1_seconds_max{application="app", class="me.mikael.mmobstest.service.HelloService", error="none", instance="host.docker.internal:8080", job="backend", method="doStuff1"}	0.0005765

doStuff2_active_seconds_count{application="app", class="me.mikael.mmobstest.service.HelloService", instance="host.docker.internal:8080", job="backend", method="doStuff2"}	0
doStuff2_active_seconds_sum{application="app", class="me.mikael.mmobstest.service.HelloService", instance="host.docker.internal:8080", job="backend", method="doStuff2"}	0
doStuff2_active_seconds_max{application="app", class="me.mikael.mmobstest.service.HelloService", instance="host.docker.internal:8080", job="backend", method="doStuff2"}	0
doStuff2_seconds_count{application="app", class="me.mikael.mmobstest.service.HelloService", error="none", instance="host.docker.internal:8080", job="backend", method="doStuff2"}	1
doStuff2_seconds_sum{application="app", class="me.mikael.mmobstest.service.HelloService", error="none", instance="host.docker.internal:8080", job="backend", method="doStuff2"}	0.0003222
doStuff2_seconds_max{application="app", class="me.mikael.mmobstest.service.HelloService", error="none", instance="host.docker.internal:8080", job="backend", method="doStuff2"}
```
- We get metrics for each Observation created; manually or via annotation


And this in the app logs
```
2025-02-22 16:16:44 INFO  [http-nio-8080-exec-4] 67b9f86c15c6c2d880c315e831720d2d,80c315e831720d2d m.m.m.controllers.HelloController - Hello there : )
2025-02-22 16:16:44 INFO  [http-nio-8080-exec-4] 67b9f86c15c6c2d880c315e831720d2d,d9629587eac3bcf0 m.m.m.controllers.HelloController - In observe lambda
2025-02-22 16:16:44 INFO  [http-nio-8080-exec-4] 67b9f86c15c6c2d880c315e831720d2d,b711f9649817bece m.m.mmobstest.service.HelloService - Doing stuff 1 in HelloService
2025-02-22 16:16:44 INFO  [http-nio-8080-exec-4] 67b9f86c15c6c2d880c315e831720d2d,5b4558c9783317a6 m.m.mmobstest.service.HelloService - Doing stuff 2 in HelloService
2025-02-22 16:16:44 INFO  [http-nio-8080-exec-4] 67b9f86c15c6c2d880c315e831720d2d,d9629587eac3bcf0 m.m.mmobstest.service.HelloService - Doing stuff 3 in HelloService
```
- New span when Observation created manually
- New span for each @Observed method
- Without @Observed like in doStuff3, just inherits the parent span. No metrics for this method
- All displaying nicely in Zipkin

## How to only create and propagate tracing spans, but not report metrics?

Tested using MessageController and MessageService.


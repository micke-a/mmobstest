package me.mikael.mmobstest.support;

import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class TracingSupport {

    private final Tracer tracer;

    public void withNewTrace(String traceId, String spanId, Map<String,String> metadata, Runnable runnable) {

        TraceContext tc = tracer.traceContextBuilder()
//                .traceId("4bf92f3577b34da6a3ce929d0e0e4736")
//                .spanId("00f067aa0ba902b7")
                .traceId(traceId)
                .spanId(spanId)
                .sampled(false)
                .build();

        try (var sc = tracer.currentTraceContext().newScope(tc)) {
            try(var baggageInScope = this.tracer.createBaggageInScope(tc,"baggage-1", "value-1")) {
                runnable.run();
            }
        }
    }

    public void withNewSpan(String spanName, Runnable runnable) {
        var newSpan = tracer.nextSpan().name(spanName);
        try(var scopedSpan = tracer.withSpan(newSpan.start())) {
            runnable.run();
        }
        finally{
            newSpan.end();
        }
    }
}

package me.mikael.mmobstest.tracing;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.propagation.Propagator;

import java.util.List;

public class CustomTracePropagator implements Propagator {
    @Override
    public List<String> fields() {
        return List.of();
    }

    @Override
    public <C> void inject(TraceContext context, C carrier, Setter<C> setter) {

    }

    @Override
    public <C> Span.Builder extract(C carrier, Getter<C> getter) {
        return null;
    }
}

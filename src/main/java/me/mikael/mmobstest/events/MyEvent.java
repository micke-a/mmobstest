package me.mikael.mmobstest.events;

import java.util.Map;

public record MyEvent(Integer id, String message, String traceId, String spanId, Map<String,String> metadata) {
}

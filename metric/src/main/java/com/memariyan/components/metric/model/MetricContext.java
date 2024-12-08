package com.memariyan.components.metric.model;

import lombok.Builder;

@Builder(toBuilder = true)
public record MetricContext(String methodName, Class<?>[] handlers,
                            Object[] inputs, Object output, Throwable error,
                            Long startTime, Long endTime) {
}

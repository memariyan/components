package com.memariyan.components.metric.handler;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class MonitoringMetricHandler implements MetricHandler {

    protected final MeterRegistry meterRegistry;

}

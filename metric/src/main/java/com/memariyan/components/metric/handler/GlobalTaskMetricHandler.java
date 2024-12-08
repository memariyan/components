package com.memariyan.components.metric.handler;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

import java.time.Duration;

public class GlobalTaskMetricHandler extends MonitoringMetricHandler {

    public static final String METRIC_NAME = "global_task";

    private final MeterRegistry meterRegistry;

    public GlobalTaskMetricHandler(MeterRegistry meterRegistry) {
        super(meterRegistry);
        this.meterRegistry = meterRegistry;
    }

    @Override
    public void handle(String methodName, Object[] inputs, Object output, Long startTime, Long endTime) {
        Timer.builder(METRIC_NAME)
                .tags("methodName", methodName, "error", "null")
                .register(meterRegistry)
                .record(Duration.ofMillis(endTime - startTime));
    }

    @Override
    public void handleError(String methodName, Object[] input, Throwable error, Long startTime, Long endTime) {
        Timer.builder(METRIC_NAME)
                .tags("methodName", methodName, "error", error.getClass().getName() + " : " + error.getMessage())
                .register(meterRegistry)
                .record(Duration.ofMillis(endTime - startTime));
    }

}

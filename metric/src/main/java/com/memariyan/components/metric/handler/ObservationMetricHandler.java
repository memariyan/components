package com.memariyan.components.metric.handler;

import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class ObservationMetricHandler implements MetricHandler {

    protected final ObservationRegistry observationRegistry;

}

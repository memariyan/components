package com.memariyan.components.metric.handler;

public interface MetricHandler {

    void handle(String methodName, Object[] inputs, Object output, Long startTime, Long endTime);

    void handleError(String methodName, Object[] input, Throwable error, Long startTime, Long endTime);

}

package com.memariyan.components.metric.annotation;

import com.memariyan.components.metric.handler.MetricHandler;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Metric {

    Class<? extends MetricHandler>[] handlers();

}

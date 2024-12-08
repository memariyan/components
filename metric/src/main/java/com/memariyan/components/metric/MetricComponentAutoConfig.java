package com.memariyan.components.metric;

import com.memariyan.components.metric.execution.MetricInterceptor;
import com.memariyan.components.metric.handler.GlobalTaskMetricHandler;
import com.memariyan.components.metric.handler.MetricHandler;
import com.memariyan.components.metric.service.MetricService;
import com.memariyan.components.metric.service.impl.MetricServiceImpl;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

@AutoConfiguration(after = {MetricHandler.class})
public class MetricComponentAutoConfig {

    @Bean
    @ConditionalOnBean(MetricHandler.class)
    public MetricService metricService(List<MetricHandler> handlers) {
        return new MetricServiceImpl(handlers);
    }

    @Bean
    @ConditionalOnBean(MetricService.class)
    public MetricInterceptor metricInterceptor(MetricService metricService) {
        return new MetricInterceptor(metricService);
    }

    @Bean
    public GlobalTaskMetricHandler globalTaskMetricHandler(MeterRegistry meterRegistry) {
        return new GlobalTaskMetricHandler(meterRegistry);
    }

}

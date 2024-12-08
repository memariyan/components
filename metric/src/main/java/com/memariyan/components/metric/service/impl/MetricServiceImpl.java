package com.memariyan.components.metric.service.impl;

import com.memariyan.components.metric.service.MetricService;
import com.memariyan.components.metric.handler.MetricHandler;
import com.memariyan.components.metric.model.MetricContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopProxyUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class MetricServiceImpl implements MetricService {

    private final Map<String, MetricHandler> handlers;

    public MetricServiceImpl(List<MetricHandler> handlers) {
        this.handlers = new HashMap<>();
        handlers.forEach(h -> this.handlers.put(AopProxyUtils.ultimateTargetClass(h).getName(), h));
    }

    @Override
    public void apply(MetricContext context) {
        getTargetHandlers(context).forEach(h -> execute(context, h));
    }

    private List<MetricHandler> getTargetHandlers(MetricContext context) {
        if (context.handlers() == null || context.handlers().length == 0) {
            return List.of();
        }
        return Arrays.stream(context.handlers()).map(handlerClass -> {
            if (!handlers.containsKey(handlerClass.getName())) {
                log.warn("There is no metric handler bean for class name {}", handlerClass.getName());
            }
            return handlers.get(handlerClass.getName());

        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private void execute(MetricContext context, MetricHandler handler) {
        try {
            if (Objects.isNull(context.error())) {
                handler.handle(context.methodName(), context.inputs(), context.output(), context.startTime(), context.endTime());
            } else {
                handler.handleError(context.methodName(), context.inputs(), context.error(), context.startTime(), context.endTime());
            }

        } catch (Throwable e) {
            log.error("error occurred while executing metric handler", e);
        }
    }

}

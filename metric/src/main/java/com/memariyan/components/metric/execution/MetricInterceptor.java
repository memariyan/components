package com.memariyan.components.metric.execution;

import com.memariyan.components.metric.annotation.Metric;
import com.memariyan.components.metric.service.MetricService;
import com.memariyan.components.metric.model.MetricContext;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

@Aspect
@RequiredArgsConstructor
public class MetricInterceptor {

    private final MetricService metricService;

    @Around("@annotation(metricAnnotation)")
    public Object handleMetrics(ProceedingJoinPoint joinPoint, Metric metricAnnotation) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        MetricContext.MetricContextBuilder context = MetricContext.builder()
                .methodName(method.getDeclaringClass().getName() + "." + method.getName())
                .handlers(metricAnnotation.handlers())
                .inputs(joinPoint.getArgs())
                .startTime(System.currentTimeMillis());

        try {
            Object result = joinPoint.proceed();
            context.output(result);
            return result;

        } catch (Throwable e) {
            context.error(e);
            throw e;

        } finally {
            context.endTime(System.currentTimeMillis());
            metricService.apply(context.build());
        }
    }

}

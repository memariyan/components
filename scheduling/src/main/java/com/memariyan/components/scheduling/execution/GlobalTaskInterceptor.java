package com.memariyan.components.scheduling.execution;

import com.memariyan.components.scheduling.annotation.GlobalTask;
import com.memariyan.components.scheduling.service.LockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.env.Environment;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@RequiredArgsConstructor
public class GlobalTaskInterceptor {

    private final LockService lockService;

    private final Environment environment;

    @Around("@annotation(annotation)")
    public Object handleGlobalTasks(ProceedingJoinPoint joinPoint, GlobalTask annotation) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName = method.getDeclaringClass().getName() + "." + method.getName();
        String serviceName = environment.getProperty("spring.application.name", "unknown");
        String lockName = serviceName + "#" + methodName;

        boolean isAcquired = lockService.acquire(lockName, annotation.waitTime(), annotation.leaseTime());
        if (!isAcquired) {
            log.debug("execution of method {} has been suspended", methodName);
            return null;
        }
        try {
            return joinPoint.proceed();

        } finally {
            lockService.release(lockName);
        }
    }

}

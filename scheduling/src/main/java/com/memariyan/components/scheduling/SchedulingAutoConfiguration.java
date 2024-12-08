package com.memariyan.components.scheduling;

import com.memariyan.components.scheduling.execution.GlobalTaskInterceptor;
import com.memariyan.components.scheduling.service.LockService;
import com.memariyan.components.scheduling.service.RedissonLockService;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@AutoConfiguration
@ConfigurationPropertiesScan
@EnableConfigurationProperties
public class SchedulingAutoConfiguration {

    @Bean
    public LockService lockService(RedissonClient client) {
        return new RedissonLockService(client);
    }

    @Bean
    @ConditionalOnBean(LockService.class)
    public GlobalTaskInterceptor globalTaskInterceptor(LockService lockService, Environment environment) {
        return new GlobalTaskInterceptor(lockService, environment);
    }

}

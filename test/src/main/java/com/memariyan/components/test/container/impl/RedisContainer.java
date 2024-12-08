package com.memariyan.components.test.container.impl;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class RedisContainer extends GenericContainerDecorator<RedisContainer> {

    public final static String RD_VERSION = "redis:5.0.3-alpine";

    @Override
    public GenericContainer<?> create() {
        try (com.redis.testcontainers.RedisContainer container = new com.redis.testcontainers.RedisContainer(
                DockerImageName.parse(RD_VERSION))) {
            return container.withExposedPorts(getExposedPort());
        }
    }

    @Override
    public void afterStart() {
        addProperty("spring.data.redis.host", getHost());
        addProperty("spring.data.redis.port", getRunningPort());
    }

    @Override
    public Integer getExposedPort() {
        return 6379;
    }

}

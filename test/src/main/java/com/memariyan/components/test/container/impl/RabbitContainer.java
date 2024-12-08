package com.memariyan.components.test.container.impl;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;

public class RabbitContainer extends GenericContainerDecorator<RabbitContainer> {

    public final static String RB_VERSION = "rabbitmq:3.8-management-alpine";

    private static final String RABBIT_DEFAULT_USERNAME = "guest";
    private static final String RABBIT_DEFAULT_PASSWORD = "guest";

    @Override
    protected GenericContainer<?> create() {
        return new RabbitMQContainer(DockerImageName.parse(RB_VERSION));
    }

    @Override
    protected void afterStart() {
        addProperty("spring.rabbitmq.host", getHost());
        addProperty("spring.rabbitmq.port", getRunningPort());
        addProperty("spring.rabbitmq.username", RABBIT_DEFAULT_USERNAME);
        addProperty("spring.rabbitmq.password", RABBIT_DEFAULT_PASSWORD);
    }

    @Override
    public Integer getExposedPort() {
        return 0;
    }
}

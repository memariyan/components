package com.memariyan.components.test.container.impl;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class KafkaContainer extends GenericContainerDecorator<KafkaContainer> {

    public final static String KF_VERSION = "confluentinc/cp-kafka:7.7.0";

    private org.testcontainers.containers.KafkaContainer container;

    @Override
    protected GenericContainer<?> create() {
        try (org.testcontainers.containers.KafkaContainer container =
                     new org.testcontainers.containers.KafkaContainer(DockerImageName.parse(KF_VERSION))) {
            this.container = container;
            return container.withKraft();
        }
    }

    @Override
    protected void afterStart() {
        addProperty("spring.kafka.bootstrap-servers", container.getBootstrapServers());
    }

    @Override
    public Integer getExposedPort() {
        return 9092;
    }
}

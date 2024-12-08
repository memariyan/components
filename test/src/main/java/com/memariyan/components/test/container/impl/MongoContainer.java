package com.memariyan.components.test.container.impl;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

public class MongoContainer extends GenericContainerDecorator<MongoContainer> {

    public final static String MN_VERSION = "mongo:7.0.14-jammy";

    @Override
    protected GenericContainer<?> create() {
        try (MongoDBContainer container = new MongoDBContainer(DockerImageName.parse(MN_VERSION))) {
            return container.withExposedPorts(getExposedPort());
        }
    }

    @Override
    protected void afterStart() {
        addProperty("spring.data.mongodb.host", getHost());
        addProperty("spring.data.mongodb.port", getRunningPort());
    }

    @Override
    public Integer getExposedPort() {
        return 27017;
    }
}

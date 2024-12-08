package com.memariyan.components.test.container.impl;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class PostgresContainer extends GenericContainerDecorator<PostgresContainer> {

    public final static String PS_VERSION = "postgres:13";

    private static final String POSTGRES_DEFAULT_USERNAME = "test";
    private static final String POSTGRES_DEFAULT_PASSWORD = "test";
    private static final String POSTGRES_DEFAULT_DB_NAME = "test";

    @Override
    public GenericContainer<?> create() {
        try (org.testcontainers.containers.PostgreSQLContainer<?> container = new org.testcontainers.containers.
                PostgreSQLContainer<>(DockerImageName.parse(PS_VERSION))) {
            return container
                    .withExposedPorts(getExposedPort())
                    .withUsername(POSTGRES_DEFAULT_USERNAME)
                    .withPassword(POSTGRES_DEFAULT_PASSWORD)
                    .withDatabaseName(POSTGRES_DEFAULT_DB_NAME);
        }
    }

    @Override
    public void afterStart() {
        addProperty("spring.datasource.url",
                String.format("jdbc:postgresql://%s:%s/%s?" +
                                "autoReconnect=true",
                        getHost(),
                        getRunningPort(),
                        POSTGRES_DEFAULT_DB_NAME));
        addProperty("spring.datasource.username", POSTGRES_DEFAULT_USERNAME);
        addProperty("spring.datasource.password", POSTGRES_DEFAULT_PASSWORD);
    }

    @Override
    public Integer getExposedPort() {
        return 5432;
    }
}
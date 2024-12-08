package com.memariyan.components.test.container.impl;

import com.memariyan.components.test.config.TestProperties;
import org.apache.commons.lang3.StringUtils;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class MySQLContainer extends GenericContainerDecorator<MySQLContainer> {

    public final static String MS_VERSION = "mysql:8.0.38";

    private static final String MYSQL_DEFAULT_USERNAME = "test_user";
    private static final String MYSQL_DEFAULT_PASSWORD = "test_pass";
    private static final String MYSQL_DEFAULT_DB_NAME = "test_db";

    private final TestProperties.MySQLConfigs configs;

    public MySQLContainer(TestProperties.MySQLConfigs configs) {
        this.configs = configs;
    }

    @Override
    public GenericContainer<?> create() {
        try (org.testcontainers.containers.MySQLContainer<?> container = new org.testcontainers.containers.
                MySQLContainer<>(DockerImageName.parse(MS_VERSION))) {
            return container
                    .withExposedPorts(getExposedPort())
                    .withUsername(MYSQL_DEFAULT_USERNAME)
                    .withPassword(MYSQL_DEFAULT_PASSWORD)
                    .withDatabaseName(getDbName());
        }
    }

    @Override
    public void afterStart() {
        addProperty("spring.datasource.url",
                String.format("jdbc:mysql://%s:%s/%s?" +
                                "autoReconnect=true" +
                                "&autoReconnectForPools=true" +
                                "&useUnicode=true" +
                                "&characterEncoding=utf8",
                        getHost(),
                        getRunningPort(),
                        getDbName()));
        addProperty("spring.datasource.username", MYSQL_DEFAULT_USERNAME);
        addProperty("spring.datasource.password", MYSQL_DEFAULT_PASSWORD);
    }

    private String getDbName() {
        return StringUtils.isBlank(configs.getDbName()) ? MYSQL_DEFAULT_DB_NAME : configs.getDbName();
    }

    @Override
    public Integer getExposedPort() {
        return 3306;
    }
}

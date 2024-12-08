package com.memariyan.components.test.container.impl;

import com.memariyan.components.test.config.TestProperties;
import org.apache.commons.lang3.StringUtils;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class MariaDBContainer extends GenericContainerDecorator<MariaDBContainer> {

    public final static String MR_VERSION = "mariadb:10.5.21";

    private static final String MARIA_DEFAULT_USERNAME = "test_user";
    private static final String MARIA_DEFAULT_PASSWORD = "test_pass";
    private static final String MARIA_DEFAULT_DB_NAME = "test_db";

    private final TestProperties.MariaDBConfigs configs;

    public MariaDBContainer(TestProperties.MariaDBConfigs configs) {
        this.configs = configs;
    }

    @Override
    public GenericContainer<?> create() {
        try (org.testcontainers.containers.MariaDBContainer<?> container = new org.testcontainers.containers.MariaDBContainer<>(
                DockerImageName.parse(MR_VERSION))) {
            return container
                    .withExposedPorts(getExposedPort())
                    .withUsername(MARIA_DEFAULT_USERNAME)
                    .withPassword(MARIA_DEFAULT_PASSWORD)
                    .withDatabaseName(getDbName());
        }
    }

    @Override
    public void afterStart() {
        addProperty("spring.datasource.url",
                String.format("jdbc:mariadb://%s:%s/%s?" +
                                "autoReconnect=true" +
                                "&autoReconnectForPools=true" +
                                "&useUnicode=true" +
                                "&characterEncoding=utf8",
                        getHost(),
                        getRunningPort(),
                        getDbName()));
        addProperty("spring.datasource.username", MARIA_DEFAULT_USERNAME);
        addProperty("spring.datasource.password", MARIA_DEFAULT_PASSWORD);
    }

    private String getDbName() {
        return StringUtils.isBlank(configs.getDbName()) ? MARIA_DEFAULT_DB_NAME : configs.getDbName();
    }

    @Override
    public Integer getExposedPort() {
        return 3306;
    }
}

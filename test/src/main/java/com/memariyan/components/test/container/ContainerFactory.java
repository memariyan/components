package com.memariyan.components.test.container;

import com.memariyan.components.test.annotation.ImageType;
import com.memariyan.components.test.config.TestPropertiesFactory;
import com.memariyan.components.test.container.impl.*;
import com.memariyan.components.test.config.TestProperties;
import org.springframework.test.context.TestContext;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.stream.Collectors;

public final class ContainerFactory {

	public Map<String, CustomContainer> create(ImageType type, TestContext testContext) {
		TestProperties properties = new TestPropertiesFactory().create(testContext.getTestClass());

		return switch (type) {
			case RABBIT -> Map.of(type.name(), new RabbitContainer());
			case KAFKA -> Map.of(type.name(), new KafkaContainer());
			case MONGO -> Map.of(type.name(), new MongoContainer());
			case MYSQL -> Map.of(type.name(), new MySQLContainer(properties.getMysql()));
			case MARIADB -> Map.of(type.name(), new MariaDBContainer(properties.getMariadb()));
			case POSTGRES -> Map.of(type.name(), new PostgresContainer());
			case REDIS -> Map.of(type.name(), new RedisContainer());
			case WIREMOCK -> getWireMockContainers(type, testContext);
		};
	}

	private Map<String, CustomContainer> getWireMockContainers(ImageType type, TestContext testContext) {
		TestProperties properties = new TestPropertiesFactory().create(testContext.getTestClass());
		if (CollectionUtils.isEmpty(properties.getWiremock())) {
			return Map.of(type.name(), new WiremockContainer(null));
		}
		return properties.getWiremock().entrySet().stream()
				.map(entry -> Map.entry(entry.getKey(), new WiremockContainer(entry.getValue())))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}
}

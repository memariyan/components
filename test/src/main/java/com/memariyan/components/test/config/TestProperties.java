package com.memariyan.components.test.config;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Getter
@Setter
@ConfigurationProperties(prefix = "com.memariyan.components.test")
public class TestProperties {

	private Map<String, WiremockConfigs> wiremock;

	private MySQLConfigs mysql;

	private MariaDBConfigs mariadb;

	@Getter
	@Setter
	public static class WiremockConfigs {

		private final static String DEFAULT_MAPPING_FOLDER_PATH = "/wiremock/mappings";

		private String mappings;

		private String record;

		private String urlReference;

		private String hosts;

		public String getMappings() {
			if (StringUtils.isBlank(mappings)) {
				return DEFAULT_MAPPING_FOLDER_PATH;
			}
			if (mappings.contains("/mappings")) {
				return mappings;
			}
			return mappings + "/mappings";
		}

	}

	@Getter
	@Setter
	public static class MySQLConfigs {

		private String dbName;

	}

	@Getter
	@Setter
	public static class MariaDBConfigs {

		private String dbName;

	}
}

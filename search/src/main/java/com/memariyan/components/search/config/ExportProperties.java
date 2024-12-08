package com.memariyan.components.search.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "com.memariyan.components.export")
public class ExportProperties {

	private Integer maxSize;

}

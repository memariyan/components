package com.memariyan.components.jpa.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "com.memariyan.components.jpa")
public class JpaProperties {

	private String attributeEncryptionKey;

}

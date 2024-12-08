package com.memariyan.components.jpa;

import com.memariyan.components.jpa.config.JpaProperties;
import com.memariyan.components.jpa.converter.AttributeEncryptionConverter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConfigurationPropertiesScan
@EnableConfigurationProperties
public class JpaComponentAutoConfig {

    @Bean
    AttributeEncryptionConverter attributeEncryptionConverter(JpaProperties properties) {
        return new AttributeEncryptionConverter(properties);
    }
}

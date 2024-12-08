package com.memariyan.components.search;

import com.memariyan.components.search.service.export.ExportService;
import com.memariyan.components.search.service.export.impl.ExportServiceImpl;
import com.memariyan.components.search.service.search.SearchService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(after = {JpaSearchAutoConfiguration.class, MongoSearchAutoConfiguration.class})
@ConfigurationPropertiesScan
@EnableConfigurationProperties
@ConditionalOnProperty(name = "com.memariyan.export.enabled", havingValue = "true")
public class ExportAutoConfiguration {

    @Bean
    @ConditionalOnBean(value = {SearchService.class})
    @ConditionalOnMissingBean
    public ExportService exportService() {
        return new ExportServiceImpl();
    }

}

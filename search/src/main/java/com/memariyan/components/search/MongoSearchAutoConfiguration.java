package com.memariyan.components.search;

import com.memariyan.components.search.presentation.mapper.EntitySearchMapper;
import com.memariyan.components.search.service.search.impl.MongoSearchServiceImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;

@AutoConfiguration
@ConditionalOnBean(value = {EntitySearchMapper.class})
@ConditionalOnClass(name = "org.springframework.data.mongodb.core.MongoOperations")
public class MongoSearchAutoConfiguration {

    @Bean(name = "MongoSearchService")
    @ConditionalOnBean(value = {MongoOperations.class})
    public MongoSearchServiceImpl mongoSearchService(MongoOperations mongoOperations) {
        return new MongoSearchServiceImpl(mongoOperations);
    }

}

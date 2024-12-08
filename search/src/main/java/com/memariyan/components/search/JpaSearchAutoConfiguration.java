package com.memariyan.components.search;

import com.memariyan.components.search.presentation.mapper.EntitySearchMapper;
import com.memariyan.components.search.service.search.impl.JpaSearchServiceImpl;
import jakarta.persistence.EntityManager;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@AutoConfiguration(after = {DataSourceAutoConfiguration.class})
@ConditionalOnBean(value = {EntitySearchMapper.class})
@ConditionalOnClass(name = "jakarta.persistence.EntityManager")
public class JpaSearchAutoConfiguration {

	@Bean(name = "JpaSearchService")
	@ConditionalOnBean(value = {DataSource.class})
	public JpaSearchServiceImpl jpaSearchService(EntityManager entityManager) {
		return new JpaSearchServiceImpl(entityManager);
	}

}

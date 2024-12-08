package com.memariyan.components.test.config;

import com.memariyan.components.test.container.ContainersContext;
import com.memariyan.components.test.extension.ExtensionsContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class EnvironmentPropertyRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware, ResourceLoaderAware {

	@Override
	public void setResourceLoader(@NonNull ResourceLoader resourceLoader) {
	}

	@Override
	public void registerBeanDefinitions(@NonNull AnnotationMetadata importingClassMetadata, @NonNull BeanDefinitionRegistry registry) {
	}

	@Override
	public void setEnvironment(@NonNull Environment env) {

		Map<String, Object> map = new HashMap<>();
		map.putAll(ContainersContext.getDefaultProperties());
		map.putAll(ExtensionsContext.getDefaultProperties());

		if (CollectionUtils.isEmpty(map)) {
			return;
		}

		String[] array = map.entrySet().stream()
				.map(e -> toPropertyEntry(e.getKey(), e.getValue()))
				.toArray(String[]::new);

		TestPropertyValues.of(array).applyTo((ConfigurableEnvironment) env);

		if (log.isTraceEnabled()) {
			Arrays.stream(array).forEach(s -> log.trace("Property updated: {}", s));
		}
	}

	private String toPropertyEntry(String key, Object newValue) {
		if (ObjectUtils.isEmpty(newValue)) {
			return key + "=";
		}
		return key + "=" + newValue;
	}
}

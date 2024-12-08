package com.memariyan.components.test.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.test.context.TestContextAnnotationUtils;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.util.TestContextResourceUtils;
import org.springframework.util.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

import static org.springframework.test.context.TestContextAnnotationUtils.findAnnotationDescriptor;

@Slf4j
public class TestPropertiesFactory {

	public TestProperties create(Class<?> clazz) {
		ConfigurableEnvironment environment = new StandardEnvironment();

		addPropertiesToEnvironment(environment, retrieveProperties(clazz));
		addLocationsToEnvironment(environment, retrieveLocations(clazz));

		TestProperties containerProperties = new TestProperties();

		try {
			String targetName = TestProperties.class.getAnnotation(ConfigurationProperties.class).prefix();

			Binder binder = new Binder(ConfigurationPropertySources.get(environment));
			containerProperties = binder.bind(targetName, TestProperties.class).get();
			log.info("Test properties loaded successfully");
		} catch (Exception e) {
			log.info("No properties exist for containers");
		}

		return containerProperties;
	}

	private String[] retrieveProperties(Class<?> clazz) {
		TestContextAnnotationUtils.AnnotationDescriptor<TestPropertySource> descriptor = findAnnotationDescriptor(clazz,
				TestPropertySource.class);
		if (descriptor == null) {
			return new String[0];
		}

		List<String> list = new ArrayList<>();

		while (descriptor != null) {
			TestPropertySource testPropertySource = descriptor.getAnnotation();

			Class<?> rootDeclaringClass = descriptor.getRootDeclaringClass();
			String[] properties = testPropertySource.properties();

			list.addAll(0, Arrays.asList(properties));

			if (!testPropertySource.inheritProperties()) {
				break;
			}

			descriptor = findAnnotationDescriptor(rootDeclaringClass.getSuperclass(), TestPropertySource.class);
		}

		return list.toArray(new String[]{});
	}

	private String[] retrieveLocations(Class<?> clazz) {
		TestContextAnnotationUtils.AnnotationDescriptor<TestPropertySource> descriptor = findAnnotationDescriptor(clazz,
				TestPropertySource.class);
		if (descriptor == null) {
			return new String[0];
		}

		Set<String> set = new HashSet<>();

		while (descriptor != null) {
			TestPropertySource testPropertySource = descriptor.getAnnotation();

			Class<?> rootDeclaringClass = descriptor.getRootDeclaringClass();
			String[] locations = testPropertySource.locations();

			if (ObjectUtils.isEmpty(testPropertySource.locations()) && ObjectUtils.isEmpty(testPropertySource.properties())) {
				String resourcePath = ClassUtils.convertClassNameToResourcePath(clazz.getName()) + ".properties";
				locations = new String[]{ResourceUtils.CLASSPATH_URL_PREFIX + resourcePath};
			}

			String[] array = TestContextResourceUtils.convertToClasspathResourcePaths(rootDeclaringClass, locations);
			set.addAll(Arrays.asList(array));

			if (!testPropertySource.inheritLocations()) {
				break;
			}

			descriptor = findAnnotationDescriptor(rootDeclaringClass.getSuperclass(), TestPropertySource.class);
		}

		return set.toArray(new String[]{});
	}

	private void addLocationsToEnvironment(ConfigurableEnvironment environment, String... locations) {
		ResourceLoader resourceLoader = new DefaultResourceLoader();

		try {
			for (String location : locations) {
				String resolvedLocation = environment.resolveRequiredPlaceholders(location);
				Resource resource = resourceLoader.getResource(resolvedLocation);

				if (resource.exists() && resource instanceof ClassPathResource) {
					environment.getPropertySources().addFirst(getPropertySource(resolvedLocation, resource));
				}
			}
		} catch (IOException ex) {
			throw new IllegalStateException("Failed to add PropertySource to Environment", ex);
		}
	}

	public PropertiesPropertySource getPropertySource(String resolvedLocation, Resource resource) throws IOException {

		if (resolvedLocation.contains(".yml")) {
			YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
			factory.setResources(resource);
			return new PropertiesPropertySource(resource.getFilename(), factory.getObject());
		} else {
			return new ResourcePropertySource(resource);
		}
	}

	private void addPropertiesToEnvironment(ConfigurableEnvironment environment, String... inlinedProperties) {
		if (!ObjectUtils.isEmpty(inlinedProperties)) {
			MapPropertySource ps = (MapPropertySource)
					environment.getPropertySources().get("Inlined Test Properties");
			if (ps == null) {
				ps = new MapPropertySource("Inlined Test Properties", new LinkedHashMap<>());
				environment.getPropertySources().addFirst(ps);
			}
			ps.getSource().putAll(convertInlinedPropertiesToMap(inlinedProperties));
		}
	}

	private Map<String, Object> convertInlinedPropertiesToMap(String... inlinedProperties) {
		Map<String, Object> map = new LinkedHashMap<>();
		Properties props = new Properties();

		for (String pair : inlinedProperties) {
			if (!StringUtils.hasText(pair)) {
				continue;
			}
			try {
				props.load(new StringReader(pair));
			} catch (Exception ex) {
				throw new IllegalStateException("Failed to load test environment property from [" + pair + "]", ex);
			}
			Assert.state(props.size() == 1, () -> "Failed to load exactly one test environment property from [" + pair + "]");
			for (String name : props.stringPropertyNames()) {
				map.put(name, props.getProperty(name));
			}
			props.clear();
		}

		return map;
	}

}

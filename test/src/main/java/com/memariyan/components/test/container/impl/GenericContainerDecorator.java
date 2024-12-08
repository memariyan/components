package com.memariyan.components.test.container.impl;

import com.memariyan.components.test.container.CustomContainer;
import org.apache.commons.lang3.StringUtils;
import org.testcontainers.containers.GenericContainer;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class GenericContainerDecorator<G extends GenericContainer<G>> extends GenericContainer<G> implements CustomContainer {

	private final Map<String, Object> defaultProperties = new HashMap<>();

	private GenericContainer<?> container;

	protected abstract GenericContainer<?> create();

	protected abstract void afterStart();

	protected void addUriProperty(String key, String url) {
		try {
			addProperty(key, new URI(url));
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	protected void addProperty(String key, Object value) {
		if (StringUtils.isEmpty(key)) {
			return;
		}

		this.defaultProperties.put(key, value);
	}

	@Override
	public void start() {
		if (Objects.isNull(this.container)) {
			this.container = create();
		}

		if (!this.container.isRunning()) {
			this.container.start();
			afterStart();
		}
	}

	@Override
	public boolean isRunning() {
		return isCreated() && container.isRunning();
	}

	public boolean isCreated() {
		return !Objects.isNull(container) && container.isCreated();
	}

	@Override
	public Integer getRunningPort() {
		if (!isCreated()) {
			return null;
		}

		return container.getMappedPort(getExposedPort());
	}

	@Override
	public String getHost() {
		return "localhost";
	}

	@Override
	public Map<String, Object> getDefaultProperties() {
		return this.defaultProperties;
	}

	@Override
	public void flush() {

	}
}

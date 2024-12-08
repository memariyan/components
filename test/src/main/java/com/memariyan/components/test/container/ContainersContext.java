package com.memariyan.components.test.container;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class ContainersContext {

	private static Map<String, CustomContainer> containers = new HashMap<>();

	public static void addContainer(String name, CustomContainer container) {
		containers.putIfAbsent(name, container);
	}

	public static void start() {
		CompletableFuture<?>[] futures = containers.values().stream()
				.filter(c -> !c.isRunning())
				.map(c -> CompletableFuture.runAsync(c::start)).toArray(CompletableFuture[]::new);

		try {
			CompletableFuture.allOf(futures).get();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void flush() {
		containers.values().forEach(CustomContainer::flush);
	}

	public static Map<String, Object> getDefaultProperties() {

		Map<String, Object> map = new HashMap<>();
		for (CustomContainer template : containers.values()) {
			map.putAll(template.getDefaultProperties());
		}
		return map;
	}

}

package com.memariyan.components.test.utils;

import java.io.*;
import java.util.Objects;

public final class TestResourceUtils {
	private final static String BASE_DIR = "src/test/resources/";

	public static File getFile(String resourceName) {
		Objects.requireNonNull(resourceName, "resource name can be null");

		if (resourceName.startsWith("/")) {
			resourceName = resourceName.substring(1);
		}

		return new File(BASE_DIR + resourceName);
	}
}

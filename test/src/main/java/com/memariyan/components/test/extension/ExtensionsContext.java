package com.memariyan.components.test.extension;

import java.util.HashMap;
import java.util.Map;

public class ExtensionsContext {

    private static Map<String, CustomExtension> extensions = new HashMap<>();

    public static void addExtension(String name, CustomExtension extension) {
        extensions.put(name, extension);
    }

    public static Map<String, Object> getDefaultProperties() {

        Map<String, Object> map = new HashMap<>();
        for (CustomExtension template : extensions.values()) {
            map.putAll(template.getDefaultProperties());
        }
        return map;
    }
}

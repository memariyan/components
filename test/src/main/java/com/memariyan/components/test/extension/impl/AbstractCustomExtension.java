package com.memariyan.components.test.extension.impl;

import com.memariyan.components.test.extension.CustomExtension;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCustomExtension implements CustomExtension {

    private final Map<String, Object> defaultProperties = new HashMap<>();

    @Override
    public Map<String, Object> getDefaultProperties() {
        return defaultProperties;
    }

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
}

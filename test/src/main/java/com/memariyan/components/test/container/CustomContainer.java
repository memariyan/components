package com.memariyan.components.test.container;


import java.util.Map;

public interface CustomContainer {

	void start();

	void flush();

	boolean isRunning();

	Integer getExposedPort();

	Integer getRunningPort();

	String getHost();

	Map<String, Object> getDefaultProperties();

}

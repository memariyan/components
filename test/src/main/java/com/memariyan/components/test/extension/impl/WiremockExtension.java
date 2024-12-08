package com.memariyan.components.test.extension.impl;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.Json;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.recording.SnapshotRecordResult;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.memariyan.components.test.config.TestProperties;
import com.memariyan.components.test.config.TestPropertiesFactory;
import com.memariyan.components.test.extension.ExtensionsContext;
import com.memariyan.components.test.utils.FileStubUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.util.CollectionUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class WiremockExtension extends AbstractCustomExtension implements BeforeAllCallback, AfterAllCallback {

	private static final String DEFAULT_WIREMOCK_HOST = "localhost";

	private static final int WIREMOCK_HTTP_START_PORT = 6000;

	private static final String CLASS_PATH_PREFIX_ADDRESS = "src/test/resources/";

	private static int lastAssignedServerPort = WIREMOCK_HTTP_START_PORT;

	private static Map<String, WireMockServer> servers = new HashMap<>();

	private Map<String, TestProperties.WiremockConfigs> propertiesMap;


	public WiremockExtension() {
		ExtensionsContext.addExtension("wiremock", this);
	}

	@Override
	public void beforeAll(ExtensionContext context) throws Exception {
		TestProperties properties = getProperties(context);
		if (CollectionUtils.isEmpty(properties.getWiremock())) {
			return;
		}
		propertiesMap = properties.getWiremock();
		propertiesMap.forEach(this::setupServer);
	}

	private void setupServer(String serverName, TestProperties.WiremockConfigs configs) {

		if (servers.containsKey(serverName)) {
			WireMockServer server = servers.get(serverName);
			Arrays.stream(configs.getUrlReference().split(","))
					.map(String::trim)
					.forEach(s -> addUriProperty(s, server.baseUrl()));
			return;
		}

		int port = lastAssignedServerPort++;
		Arrays.stream(configs.getUrlReference().split(",")).map(String::trim)
				.forEach(s -> addUriProperty(s, "http://" + DEFAULT_WIREMOCK_HOST + ":" + port));

		WireMockServer server = startServer(configs.getMappings(), configs.getRecord(), port);
		servers.put(serverName, server);
	}

	private WireMockServer startServer(String mappingsFolder, String recordUrl, int port) {
		WireMockServer server = new WireMockServer(WireMockConfiguration.options()
				.usingFilesUnderClasspath(CLASS_PATH_PREFIX_ADDRESS + mappingsFolder.replace("/mappings", ""))
				.globalTemplating(true)
				.bindAddress(DEFAULT_WIREMOCK_HOST).port(port));

		server.start();

		if (StringUtils.isNotBlank(recordUrl)) {
			server.startRecording(recordUrl);
		}
		return server;
	}

	@Override
	public void afterAll(ExtensionContext context) throws Exception {
		for (Map.Entry<String, WireMockServer> server : servers.entrySet()) {
			flushServer(server.getKey(), server.getValue());
		}
	}

	private void flushServer(String serverKey, WireMockServer server) throws IOException {
		if (!server.isRunning()) {
			return;
		}
		TestProperties.WiremockConfigs configs = propertiesMap.get(serverKey);

		if (!StringUtils.isBlank(configs.getRecord())) {
			SnapshotRecordResult recordResult = server.stopRecording();

			for (StubMapping mapping : recordResult.getStubMappings()) {
				String fileName = FileStubUtils.makeSafeFileName(mapping);
				String content = Json.write(mapping);

				BufferedWriter writer = new BufferedWriter(new FileWriter(CLASS_PATH_PREFIX_ADDRESS +
						configs.getMappings() + "/" + fileName));
				writer.write(content);
				writer.close();
			}
		}
		server.resetAll();
	}

	private TestProperties getProperties(ExtensionContext context) throws IOException {
		return new TestPropertiesFactory().create(context.getRequiredTestClass());
	}
}

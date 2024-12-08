package com.memariyan.components.test.container.impl;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.Json;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.memariyan.components.test.config.TestProperties;
import com.memariyan.components.test.utils.FileStubUtils;
import com.memariyan.components.test.utils.TestResourceUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
public class WiremockContainer extends GenericContainerDecorator<WiremockContainer> {

	public final static String WM_VERSION = "wiremock/wiremock:3.8.0";

	private final String urlReference;

	private final String mappingsFolder;

	private final String record;

	private final String hosts;

	public WiremockContainer(TestProperties.WiremockConfigs configs) {
        if (Objects.isNull(configs)) {
			configs = new TestProperties.WiremockConfigs();
		}
		this.mappingsFolder = configs.getMappings();
		this.urlReference = configs.getUrlReference();
		this.record = configs.getRecord();
		this.hosts = configs.getHosts();
	}

	@Override
	public GenericContainer<?> create() {
		GenericContainer<WiremockContainer> container = new GenericContainer<WiremockContainer>(DockerImageName.parse(WM_VERSION))
				.withExposedPorts(getExposedPort());
		File mappingDir = TestResourceUtils.getFile(this.mappingsFolder);

		if (!mappingDir.exists()) {
			mappingDir.mkdirs();
		}

		MountableFile file = MountableFile.forHostPath(mappingDir.getAbsolutePath());
		container = container.withCopyFileToContainer(file, "/home/wiremock/mappings");

		if (!StringUtils.isEmpty(this.record)) {
			container = container.withCommand("--record-mappings --proxy-all " + this.record);
		}

		if (!StringUtils.isEmpty(hosts)) {
			String[] hostArray = this.hosts.split(",");
			for (String host : hostArray) {
				String[] hostParts = host.trim().split(":");
				container = container.withExtraHost(hostParts[0].trim(), hostParts[1].trim());
			}
		}
		return container;
	}

	@Override
	public void afterStart() {
		if (StringUtils.isEmpty(this.urlReference)) {
			return;
		}

		Arrays.stream(this.urlReference.split(",")).map(String::trim)
				.forEach(s -> addUriProperty(s, "http://" + getHost() + ":" + getRunningPort()));
	}

	@Override
	public void flush() {
		if (StringUtils.isEmpty(this.record)) {
			return;
		}

		WireMock wireMock = new WireMock(getHost(), getRunningPort());
		List<StubMapping> stubMappings = wireMock.takeSnapshotRecording();

		if (Objects.isNull(stubMappings)) {
			return;
		}

		try {
			String path = TestResourceUtils.getFile(this.mappingsFolder).getAbsolutePath();
			for (StubMapping stubMapping : stubMappings) {
				String fileName = FileStubUtils.makeSafeFileName(stubMapping);
				String content = Json.write(stubMapping);

				BufferedWriter writer = new BufferedWriter(new FileWriter(path + "/" + fileName));
				writer.write(content);
				writer.close();
			}
		} catch (IOException ex) {
			log.info("error while writing stub mapping file", ex);
		}
	}

	@Override
	public Integer getExposedPort() {
		return 8080;
	}

}

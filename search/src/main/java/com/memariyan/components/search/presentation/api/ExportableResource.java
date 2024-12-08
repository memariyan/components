package com.memariyan.components.search.presentation.api;

import com.memariyan.components.common.dto.BaseDTO;
import com.memariyan.components.common.model.BaseEntity;
import com.memariyan.components.common.model.HeaderNames;
import com.memariyan.components.search.service.export.ExportService;
import com.memariyan.components.search.config.ExportProperties;
import com.memariyan.components.search.presentation.model.request.ExportRequest;
import com.memariyan.components.search.presentation.mapper.ExportItemInfoContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.validation.Valid;
import java.util.UUID;

public interface ExportableResource<T extends BaseEntity, E extends BaseDTO> extends SearchableResource<T, E> {

	Logger LOG = LoggerFactory.getLogger(ExportableResource.class);

	ExportProperties getExportProperties();

	ExportService getExportService();

	ExportItemInfoContext<T> getExportItemInfo();

	@PostMapping(path = "/export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	default ResponseEntity<ByteArrayResource> export(
			@RequestHeader(value = HeaderNames.USER_ID) String userId,
			@Valid @RequestBody final ExportRequest exportRequest) throws Exception {

		LOG.debug("export api gonna processed with request = {}", exportRequest);

		Page<T> searchResult = doSearch(userId, exportRequest, PageRequest.of(0, getExportProperties().getMaxSize()));
		LOG.debug("export search result: {} with total elements of: {}", searchResult.getContent(),
				searchResult.getTotalElements());
		if (searchResult.getTotalElements() > getExportProperties().getMaxSize()) {
			throw new IllegalStateException("export result is exceeded the maximum size");
		}

		byte[] result = getExportService().exportAsExcel(searchResult, getSearchTargetClass(), exportRequest.getFields(),
				getExportItemInfo().getItemMapping());

		ByteArrayResource resource = new ByteArrayResource(result);
		String fileName = exportRequest.getFileName() != null ? exportRequest.getFileName() : UUID.randomUUID().toString();
		LOG.debug("export result name: {}, size: {}", fileName, resource.contentLength());
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(resource);
	}

}

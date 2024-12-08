package com.memariyan.components.search.presentation.api;

import com.memariyan.components.common.model.BaseEntity;
import com.memariyan.components.common.model.HeaderNames;
import com.memariyan.components.search.presentation.mapper.EntitySearchMapper;
import com.memariyan.components.search.presentation.model.request.SearchRequest;
import com.memariyan.components.search.presentation.model.response.SearchResponse;
import com.memariyan.components.common.dto.BaseDTO;
import com.memariyan.components.search.service.model.SearchCriteria;
import com.memariyan.components.search.service.search.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.validation.Valid;

public interface SearchableResource<T extends BaseEntity, E extends BaseDTO> {

	Logger LOG = LoggerFactory.getLogger(SearchableResource.class);

	EntitySearchMapper<T, E> getSearchMapper();

	SearchService getSearchService();

	Class<T> getSearchTargetClass();

	@PostMapping(path = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
	default ResponseEntity<SearchResponse<E>> search(
			@RequestHeader(value = HeaderNames.USER_ID) String userId,
			@Valid @RequestBody final SearchRequest searchRequest,
			Pageable pageable) throws Exception {

		LOG.debug("going to search with request = {}", searchRequest);
		Page<T> searchResult = doSearch(userId, searchRequest, pageable);
		SearchResponse<E> response = getSearchMapper().toSearchResponse(searchResult);
		return ResponseEntity.ok(response);
	};

	default void addCurrentUserRestriction(String requesterUserId, SearchRequest searchRequest) throws Exception {
	}

	default Page<T> doSearch(String requesterUserId, SearchRequest searchRequest, Pageable pageable) throws Exception {
		this.addCurrentUserRestriction(requesterUserId, searchRequest);
		SearchCriteria searchCriteria = getSearchMapper().toSearchCriteria(searchRequest);
		return this.getSearchService().search(searchCriteria, getSearchTargetClass(), pageable);
	}
}

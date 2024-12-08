package com.memariyan.components.search.presentation.mapper;

import com.memariyan.components.common.dto.BaseDTO;
import com.memariyan.components.common.model.BaseEntity;
import com.memariyan.components.search.presentation.model.request.SearchRequest;
import com.memariyan.components.search.presentation.model.response.SearchResponse;
import com.memariyan.components.search.service.model.SearchCriteria;
import org.springframework.data.domain.Page;

public interface EntitySearchMapper<T extends BaseEntity, E extends BaseDTO> {

    SearchCriteria toSearchCriteria(SearchRequest searchRequest);

    SearchResponse<E> toSearchResponse(Page<T> searchResult);

    SearchFieldInfoContext getSearchFieldInfoContext(SearchRequest searchRequest);

}

package com.memariyan.components.search.service.search;

import com.memariyan.components.common.model.BaseEntity;
import com.memariyan.components.search.service.model.SearchCriteria;
import com.memariyan.components.search.service.model.StorageType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchService {

	<T extends BaseEntity> Page<T> search(SearchCriteria searchCriteria, Class<T> subjectClass, Pageable pageable);

	StorageType getStorageType();

}

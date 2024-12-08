package com.memariyan.components.search.service.model.restriction;

import com.memariyan.components.common.model.BaseEntity;
import com.memariyan.components.search.presentation.mapper.SearchFieldInfoContext;
import com.memariyan.components.search.presentation.model.request.dto.RestrictionDto;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.mongodb.core.query.Criteria;

public interface SearchRestriction {

	Criteria fillCriteria(Criteria criteria);

	<T extends BaseEntity> Predicate fillCriteria(CriteriaBuilder builder, Root<T> root);

	void setStorageFieldNames(SearchFieldInfoContext fieldInfoContext, RestrictionDto restrictionDto);

}

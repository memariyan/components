package com.memariyan.components.search.service.model.restriction;

import com.memariyan.components.common.model.BaseEntity;
import com.memariyan.components.search.presentation.model.request.dto.RestrictionDto;
import com.memariyan.components.search.presentation.model.request.dto.restrictions.AndRestrictionDto;
import com.memariyan.components.search.presentation.mapper.SearchFieldInfoContext;
import com.memariyan.components.search.presentation.mapper.impl.RestrictionDataMapper;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AndRestriction implements SearchRestriction {

	private List<SearchRestriction> restrictions;

	@Override
	public Criteria fillCriteria(Criteria criteria) {
		Criteria[] criteriaArray = new Criteria[this.restrictions.size()];

		for (int i = 0; i < this.restrictions.size(); i++) {
			criteriaArray[i] = this.restrictions.get(i).fillCriteria(new Criteria());
		}

		return criteria.andOperator(criteriaArray);
	}

	@Override
	public <T extends BaseEntity> Predicate fillCriteria(CriteriaBuilder builder, Root<T> root) {
		List<Predicate> predicates = new ArrayList<>();

		CollectionUtils.emptyIfNull(restrictions).forEach(restriction -> predicates
				.add(restriction.fillCriteria(builder, root)));

		return builder.and(predicates.toArray(new Predicate[0]));
	}

	@Override
	public void setStorageFieldNames(SearchFieldInfoContext fieldInfoContext, RestrictionDto andRestrictionDto) {
		List<RestrictionDto> restrictions = ((AndRestrictionDto) andRestrictionDto).getRestrictions();

		this.restrictions = CollectionUtils.emptyIfNull(restrictions).stream().map(restrictionDto ->
				RestrictionDataMapper.getSearchRestriction(restrictionDto, fieldInfoContext)).collect(Collectors.toList());

	}
}

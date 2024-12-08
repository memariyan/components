package com.memariyan.components.search.service.model.restriction;

import com.memariyan.components.common.model.BaseEntity;
import com.memariyan.components.search.presentation.mapper.FieldInfo;
import com.memariyan.components.search.presentation.mapper.SearchFieldInfoContext;
import com.memariyan.components.search.presentation.model.request.dto.RestrictionDto;
import com.memariyan.components.search.presentation.model.request.dto.restrictions.InRangeRestrictionDto;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.query.Criteria;

@Setter
@Getter
@ToString
public class InRangeRestriction extends AbstractSearchRestriction {

	private Object maxValue;

	private Object minValue;

	@Override
	public Criteria fillCriteria(Criteria criteria) {
		if (this.minValue != null && this.maxValue != null) {
			return criteria.and(this.getField()).gte(minValue).lte(maxValue);
		} else if (this.minValue != null) {
			return criteria.and(this.getField()).gte(minValue);
		} else if (this.maxValue != null) {
			return criteria.and(this.getField()).lte(maxValue);
		}
		throw new IllegalStateException("Invalid in range restriction, fill min or max value or both of them");
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public <T extends BaseEntity> Predicate fillCriteria(CriteriaBuilder builder, Root<T> root) {
		if (this.minValue != null && this.maxValue != null) {
			return builder.between(root.get(this.getField()), (Comparable) this.minValue, (Comparable) this.maxValue);
		} else if (this.minValue != null) {
			return builder.greaterThanOrEqualTo(root.get(this.getField()), (Comparable) this.minValue);
		} else if (this.maxValue != null) {
			return builder.lessThanOrEqualTo(root.get(this.getField()), (Comparable) this.maxValue);
		}
		throw new IllegalStateException("Invalid in range restriction, fill min or max value or both of them");
	}

	@Override
	public void setStorageFieldNames(SearchFieldInfoContext fieldInfoContext, RestrictionDto inRangeRestrictionDto) {
		InRangeRestrictionDto restrictionDto = (InRangeRestrictionDto) inRangeRestrictionDto;
		FieldInfo fieldInfo = fieldInfoContext.findFieldInfo(restrictionDto.getField());
		this.setField(fieldInfo.getMappedPropertyName());

		if (fieldInfo.getMapper() != null) {
			this.setMinValue(fieldInfo.getMapper().apply(restrictionDto.getMinValue()));
			this.setMaxValue(fieldInfo.getMapper().apply(restrictionDto.getMaxValue()));
		} else {
			this.setMaxValue(restrictionDto.getMaxValue());
			this.setMinValue(restrictionDto.getMinValue());
		}
	}
}

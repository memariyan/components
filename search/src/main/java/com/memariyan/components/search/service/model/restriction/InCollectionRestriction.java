package com.memariyan.components.search.service.model.restriction;

import com.memariyan.components.common.model.BaseEntity;
import com.memariyan.components.search.presentation.model.request.dto.RestrictionDto;
import com.memariyan.components.search.presentation.model.request.dto.restrictions.InCollectionRestrictionDto;
import com.memariyan.components.search.presentation.mapper.FieldInfo;
import com.memariyan.components.search.presentation.mapper.SearchFieldInfoContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Setter
@Getter
@ToString
public class InCollectionRestriction extends AbstractSearchRestriction {

	private Collection<?> values;

	@Override
	public Criteria fillCriteria(Criteria criteria) {
		return criteria.and(this.getField()).in(values);
	}

	@Override
	public <T extends BaseEntity> Predicate fillCriteria(CriteriaBuilder builder, Root<T> root) {
		String[] fieldNames = this.getField().split("[.]");
		return findField(root.get(fieldNames[0]), fieldNames, 0).in(this.values);
	}

	private <Y> Path<Y> findField(Path<Y> root, String[] fieldNames, int index) {
		if (fieldNames.length == index + 1) {
			return root;
		}
		index++;
		return findField(root.get(fieldNames[index]), fieldNames, index);
	}

	@Override
	public void setStorageFieldNames(SearchFieldInfoContext fieldInfoContext, RestrictionDto inCollectionRestrictionDto) {
		InCollectionRestrictionDto restrictionDto = (InCollectionRestrictionDto) inCollectionRestrictionDto;
		FieldInfo fieldInfo = fieldInfoContext.findFieldInfo(restrictionDto.getField());
		this.setField(fieldInfo.getMappedPropertyName());

		if (restrictionDto.getValues() == null) {
			this.values = new ArrayList<>();
		} else if (fieldInfo.getMapper() != null) {
			this.setValues((List) fieldInfo.getMapper().apply(restrictionDto.getValues()));
		} else {
			this.setValues(restrictionDto.getValues());
		}
	}
}

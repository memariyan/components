package com.memariyan.components.search.service.model.restriction;

import com.memariyan.components.common.model.BaseEntity;
import com.memariyan.components.search.presentation.model.request.dto.RestrictionDto;
import com.memariyan.components.search.presentation.model.request.dto.restrictions.SimpleValueRestrictionDto;
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

@Setter
@Getter
@ToString
public class SimpleValueRestriction extends AbstractSearchRestriction {

	private OperationType operationType;

	private Object value;

	public enum OperationType {
		EQ,
		NE,
		GT,
		LT,
		GE,
		LE;

	}

	@Override
	public Criteria fillCriteria(Criteria criteria) {
		criteria = criteria.and(this.getField());

		switch (this.operationType) {
			case EQ:
				criteria = criteria.is(this.value);
				break;
			case NE:
				criteria = criteria.ne(this.value);
				break;
			case GT:
				criteria = criteria.gt(this.value);
				break;
			case GE:
				criteria = criteria.gte(this.value);
				break;
			case LT:
				criteria = criteria.lt(this.value);
				break;
			case LE:
				criteria = criteria.lte(this.value);
				break;
			default:
				throw new IllegalStateException("invalid operation type=" + this.operationType);
		}

		return criteria;

	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public <T extends BaseEntity> Predicate fillCriteria(CriteriaBuilder builder, Root<T> root) {
		String[] fieldNames = this.getField().split("[.]");

        return switch (this.operationType) {
            case EQ -> builder.equal(findField(root.get(fieldNames[0]), fieldNames, 0), this.getValue());
            case NE -> builder.notEqual(findField(root.get(fieldNames[0]), fieldNames, 0), this.getValue());
            case GT -> builder.greaterThan(findField(root.get(fieldNames[0]), fieldNames, 0), (Comparable) this.getValue());
            case GE -> builder.greaterThanOrEqualTo(findField(root.get(fieldNames[0]), fieldNames, 0), (Comparable) this.getValue());
            case LT -> builder.lessThan(findField(root.get(fieldNames[0]), fieldNames, 0), (Comparable) this.getValue());
            case LE -> builder.lessThanOrEqualTo(findField(root.get(fieldNames[0]), fieldNames, 0), (Comparable) this.getValue());
            default -> throw new IllegalStateException("invalid operation type=" + this.operationType);
        };

	}

	private <Y> Path<Y> findField(Path<Y> root, String[] fieldNames, int index) {
		if (fieldNames.length == index + 1) {
			return root;
		}
		index++;
		return findField(root.get(fieldNames[index]), fieldNames, index);
	}

	@Override
	public void setStorageFieldNames(SearchFieldInfoContext context, RestrictionDto simpleRestrictionDto) {
		SimpleValueRestrictionDto restrictionDto = (SimpleValueRestrictionDto) simpleRestrictionDto;
		FieldInfo fieldInfo = context.findFieldInfo(simpleRestrictionDto.getField());
		this.setField(fieldInfo.getMappedPropertyName());
		this.setOperationType(OperationType.valueOf(restrictionDto.getOperation().name()));

		if (fieldInfo.getMapper() != null) {
			this.setValue(fieldInfo.getMapper().apply(restrictionDto.getValue()));
		} else {
			this.setValue(restrictionDto.getValue());
		}
	}
}

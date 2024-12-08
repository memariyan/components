package com.memariyan.components.search.presentation.model.request.dto.restrictions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.memariyan.components.search.presentation.model.request.dto.RestrictionDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SimpleValueRestrictionDto extends RestrictionDto {

	private Object value;

	private OperationType operation;

	public SimpleValueRestrictionDto() {
	}

	public SimpleValueRestrictionDto(String field, Object value, OperationType operationType) {
		this.setField(field);
		this.value = value;
		this.operation = operationType;
	}

	public enum OperationType {
		EQ,
		NE,
		GT,
		LT,
		GE,
		LE;

		@JsonCreator
		public static OperationType forValue(String value) {
			return OperationType.valueOf(value.toUpperCase());
		}

		@JsonValue
		public String toValue() {
			return this.name().toLowerCase();
		}
	}
}

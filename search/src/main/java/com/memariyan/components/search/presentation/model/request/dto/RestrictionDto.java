package com.memariyan.components.search.presentation.model.request.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.memariyan.components.search.presentation.model.request.dto.restrictions.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "type")
@JsonSubTypes({
		@Type(name = "simple", value = SimpleValueRestrictionDto.class),
		@Type(name = "collection", value = InCollectionRestrictionDto.class),
		@Type(name = "range", value = InRangeRestrictionDto.class),
		@Type(name = "or", value = OrRestrictionDto.class),
		@Type(name = "and", value = AndRestrictionDto.class)
})
public abstract class RestrictionDto {

	private String field;

	public boolean fieldBasedRestriction() {
		return true;
	}

}

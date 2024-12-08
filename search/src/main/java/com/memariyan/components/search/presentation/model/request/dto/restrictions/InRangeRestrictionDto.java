package com.memariyan.components.search.presentation.model.request.dto.restrictions;

import com.memariyan.components.search.presentation.model.request.dto.RestrictionDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class InRangeRestrictionDto extends RestrictionDto {

	private Object minValue;

	private Object maxValue;

}

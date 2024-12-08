package com.memariyan.components.search.presentation.model.request.dto.restrictions;

import com.memariyan.components.search.presentation.model.request.dto.RestrictionDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collection;

@Setter
@Getter
@ToString
public class InCollectionRestrictionDto extends RestrictionDto {

	private Collection<? extends Object> values;
}

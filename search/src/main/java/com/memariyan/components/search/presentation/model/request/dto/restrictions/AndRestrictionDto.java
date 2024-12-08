package com.memariyan.components.search.presentation.model.request.dto.restrictions;

import com.memariyan.components.search.presentation.model.request.dto.RestrictionDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Setter
@ToString
@Getter
public class AndRestrictionDto extends RestrictionDto {

	@Getter(AccessLevel.NONE)
	private List<RestrictionDto> restrictions;

	public List<RestrictionDto> getRestrictions() {
		if (restrictions == null)
			restrictions = new ArrayList<>();
		return restrictions;
	}

	@Override
	public boolean fieldBasedRestriction() {
		return false;
	}

}

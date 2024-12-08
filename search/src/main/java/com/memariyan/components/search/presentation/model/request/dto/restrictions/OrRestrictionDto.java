package com.memariyan.components.search.presentation.model.request.dto.restrictions;

import com.memariyan.components.search.presentation.model.request.dto.RestrictionDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
public class OrRestrictionDto extends RestrictionDto {

	private List<RestrictionDto> restrictions;

	public void addToRestrictions(RestrictionDto restrictionDTO) {
		if (CollectionUtils.isEmpty(this.restrictions))
			this.restrictions = new ArrayList<>();

		this.restrictions.add(restrictionDTO);
	}

	@Override
	public boolean fieldBasedRestriction() {
		return false;
	}


}

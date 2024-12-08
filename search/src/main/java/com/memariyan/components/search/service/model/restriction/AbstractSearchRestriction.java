package com.memariyan.components.search.service.model.restriction;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public abstract class AbstractSearchRestriction implements SearchRestriction {

	private String field;

}

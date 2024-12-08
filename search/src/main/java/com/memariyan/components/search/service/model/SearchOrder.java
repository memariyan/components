package com.memariyan.components.search.service.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SearchOrder {

	private String field;

	private String order;
}

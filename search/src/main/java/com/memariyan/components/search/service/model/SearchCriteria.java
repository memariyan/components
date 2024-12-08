package com.memariyan.components.search.service.model;

import com.memariyan.components.search.service.model.restriction.SearchRestriction;
import lombok.Getter;
import lombok.ToString;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class SearchCriteria {

	private List<SearchOrder> orders;

	private List<SearchRestriction> restrictions;

	public void addToOrders(SearchOrder order) {
		if (orders == null) {
			orders = new ArrayList<>();
		}
		orders.add(order);
	}

	public void addToRestrictions(SearchRestriction restriction) {
		if (restrictions == null) {
			restrictions = new ArrayList<>();
		}
		restrictions.add(restriction);
	}


}

package com.memariyan.components.search.presentation.model.request;

import com.memariyan.components.common.request.BaseRequest;
import com.memariyan.components.search.presentation.model.request.dto.OrderDto;
import com.memariyan.components.search.presentation.model.request.dto.RestrictionDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
public class SearchRequest extends BaseRequest {

	@Getter(AccessLevel.NONE)
	private List<RestrictionDto> restrictions;

	@Getter(AccessLevel.NONE)
	private List<OrderDto> orders;

	public List<RestrictionDto> getRestrictions() {
		if (restrictions == null)
			restrictions = new ArrayList<>(3);
		return restrictions;
	}

	public void addToRestrictions(RestrictionDto restrictionDTO) {
		if (this.restrictions == null)
			this.restrictions = new ArrayList<>();

		this.restrictions.add(restrictionDTO);
	}

	public List<OrderDto> getOrders() {
		if (orders == null)
			orders = new ArrayList<>(3);
		return orders;
	}

	public void addToOrders(OrderDto orderDto) {
		if (orders == null)
			orders = new ArrayList<>(3);
		this.orders.add(orderDto);
	}
}

package com.memariyan.components.search.presentation.mapper.impl;

import com.memariyan.components.common.dto.BaseDTO;
import com.memariyan.components.common.model.BaseEntity;
import com.memariyan.components.search.presentation.model.request.SearchRequest;
import com.memariyan.components.search.presentation.model.request.dto.OrderDto;
import com.memariyan.components.search.presentation.model.request.dto.RestrictionDto;
import com.memariyan.components.search.presentation.mapper.EntitySearchMapper;
import com.memariyan.components.search.presentation.mapper.SearchFieldInfoContext;
import com.memariyan.components.search.presentation.mapper.FieldInfo;
import com.memariyan.components.search.service.model.SearchCriteria;
import com.memariyan.components.search.service.model.SearchOrder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public interface DefaultEntitySearchMapperImpl<T extends BaseEntity, E extends BaseDTO> extends EntitySearchMapper<T, E> {

    Logger LOG = LoggerFactory.getLogger(DefaultEntitySearchMapperImpl.class);

    default SearchCriteria toSearchCriteria(SearchRequest searchRequest) {
        List<RestrictionDto> restrictionDtos = searchRequest.getRestrictions();
        List<OrderDto> orderDtos = searchRequest.getOrders();

        SearchCriteria searchCriteria = new SearchCriteria();
        SearchFieldInfoContext infoContext = getSearchFieldInfoContext(searchRequest);
        CollectionUtils.emptyIfNull(restrictionDtos)
                .forEach(restrictionDto -> searchCriteria.addToRestrictions(
                        RestrictionDataMapper.getSearchRestriction(restrictionDto, infoContext)));

        if (CollectionUtils.isNotEmpty(orderDtos)) {
            LOG.debug("Sorting search result ");
            for (OrderDto orderDto : orderDtos) {
                LOG.debug("Sorting by field : {} and order : {} ", orderDto.getField(), orderDto.getOrder());

                FieldInfo<?, ?> info = infoContext.findFieldInfo(orderDto.getField());
                if (info == null)
                    throw new IllegalStateException("There is not any field with name "
                            + orderDto.getField() + " in search context");
                SearchOrder searchOrder = new SearchOrder();
                searchOrder.setField(info.getMappedPropertyName());
                if (StringUtils.isBlank(orderDto.getOrder())) {
                    searchOrder.setOrder("asc");
                }
                if (orderDto.getOrder().equals("asc") || orderDto.getOrder().equals("desc")) {
                    searchOrder.setOrder(orderDto.getOrder());
                } else {
                    throw new IllegalStateException("Wrong search order : " + orderDto.getOrder());
                }
                searchCriteria.addToOrders(searchOrder);
            }
        }
        return searchCriteria;
    }

}

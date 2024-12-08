package com.memariyan.components.search.service.search.impl;

import com.memariyan.components.common.model.BaseEntity;
import com.memariyan.components.search.service.search.SearchService;
import com.memariyan.components.search.service.model.SearchCriteria;
import com.memariyan.components.search.service.model.SearchOrder;
import com.memariyan.components.search.service.model.StorageType;
import com.memariyan.components.search.service.model.restriction.SearchRestriction;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@RequiredArgsConstructor
public class MongoSearchServiceImpl implements SearchService {

	private final MongoOperations mongoOperations;

	@Override
	public <T extends BaseEntity> Page<T> search(SearchCriteria searchCriteria, Class<T> subjectClass, Pageable pageable) {
		Query query = createMongoQuery(searchCriteria);
		List<T> items = mongoOperations.find(query.with(pageable), subjectClass);
		long count = mongoOperations.count(query, subjectClass);
		return new PageImpl<>(items, pageable, count);
	}

	@Override
	public StorageType getStorageType() {
		return StorageType.MONGO;
	}

	public Query createMongoQuery(SearchCriteria searchCriteria) {
		Query query = new Query();

		if (CollectionUtils.isNotEmpty(searchCriteria.getRestrictions())) {
			Criteria criteria = new Criteria();

			for (SearchRestriction restriction : searchCriteria.getRestrictions()) {
				criteria = restriction.fillCriteria(criteria);
			}

			query.addCriteria(criteria);
		}

		if (CollectionUtils.isNotEmpty(searchCriteria.getOrders())) {

			for (SearchOrder searchOrder : searchCriteria.getOrders()) {
				Sort.Direction sortDirection =
						searchOrder.getOrder().equalsIgnoreCase("desc") ?
						Sort.Direction.DESC : Sort.Direction.ASC;

				query.with(Sort.by(sortDirection, searchOrder.getField()));
			}
		}
		return query;
	}

}

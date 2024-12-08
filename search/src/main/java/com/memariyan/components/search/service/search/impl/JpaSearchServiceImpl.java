package com.memariyan.components.search.service.search.impl;

import com.memariyan.components.common.model.BaseEntity;
import com.memariyan.components.search.service.model.SearchCriteria;
import com.memariyan.components.search.service.model.StorageType;
import com.memariyan.components.search.service.search.SearchService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JpaSearchServiceImpl implements SearchService {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public <T extends BaseEntity> Page<T> search(SearchCriteria searchCriteria, Class<T> subjectClass, Pageable pageable) {
        CriteriaQuery<T> criteria = createCriteria(searchCriteria, entityManager.getCriteriaBuilder(), subjectClass);
        List<T> items = entityManager.createQuery(criteria)
                .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        CriteriaQuery<Long> countQuery = createCountCriteria(searchCriteria, subjectClass);
        long count = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(items, pageable, count);
    }

    @Override
    public StorageType getStorageType() {
        return StorageType.JPA;
    }

    public <T extends BaseEntity> CriteriaQuery<T> createCriteria(
            SearchCriteria searchCriteria, CriteriaBuilder builder, Class<T> subjectClass) {
        CriteriaQuery<T> criteria = builder.createQuery(subjectClass);
        Root<T> root = criteria.from(subjectClass);
        criteria.select(root);
        fillFilterCriteria(searchCriteria, criteria, builder, root);
        fillSortingCriteria(searchCriteria, criteria, builder, root);
        return criteria;
    }

    public <T extends BaseEntity> CriteriaQuery<Long> createCountCriteria(SearchCriteria searchCriteria, Class<T> subjectClass) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<T> root = criteria.from(subjectClass);
        criteria.select(builder.count(root));
        fillFilterCriteria(searchCriteria, criteria, builder, root);
        return criteria;
    }

    private <T extends BaseEntity, R> void fillFilterCriteria(SearchCriteria searchCriteria,
                                                              CriteriaQuery<R> criteria, CriteriaBuilder builder, Root<T> root) {

        if (CollectionUtils.isNotEmpty(searchCriteria.getRestrictions())) {
            Predicate[] predicates = new Predicate[searchCriteria.getRestrictions().size()];
            for (int i = 0; i < predicates.length; i++) {
                predicates[i] = (searchCriteria.getRestrictions().get(i).fillCriteria(builder, root));
            }
            criteria.where(builder.and(predicates));
        }
    }

    private <T extends BaseEntity, R> void fillSortingCriteria(SearchCriteria searchCriteria,
                                                               CriteriaQuery<R> criteria, CriteriaBuilder builder, Root<T> root) {

        if (CollectionUtils.isNotEmpty(searchCriteria.getOrders())) {
            criteria.orderBy(searchCriteria.getOrders().stream().map(order -> order.getOrder().equalsIgnoreCase("desc") ?
                    builder.desc(root.get(order.getField())) :
                    builder.asc(root.get(order.getField()))).collect(Collectors.toList()));
        }
    }
}

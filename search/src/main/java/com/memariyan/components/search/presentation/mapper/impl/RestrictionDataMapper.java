package com.memariyan.components.search.presentation.mapper.impl;

import com.memariyan.components.search.presentation.mapper.SearchFieldInfoContext;
import com.memariyan.components.search.presentation.model.request.dto.RestrictionDto;
import com.memariyan.components.search.presentation.model.request.dto.restrictions.*;
import com.memariyan.components.search.service.model.restriction.*;

public final class RestrictionDataMapper {

    private RestrictionDataMapper() {

    }

    public static SearchRestriction getSearchRestriction(RestrictionDto restrictionDto, SearchFieldInfoContext context) {
        SearchRestriction restriction = getSearchRestrictionInstance(restrictionDto.getClass());
        restriction.setStorageFieldNames(context, restrictionDto);
        return restriction;
    }

    private static SearchRestriction getSearchRestrictionInstance(Class<? extends RestrictionDto> restrictionDtoClass) {
        if (restrictionDtoClass.isAssignableFrom(AndRestrictionDto.class)) {
            return new AndRestriction();
        } else if (restrictionDtoClass.isAssignableFrom(OrRestrictionDto.class)) {
            return new OrRestriction();
        } else if (restrictionDtoClass.isAssignableFrom(InCollectionRestrictionDto.class)) {
            return new InCollectionRestriction();
        } else if (restrictionDtoClass.isAssignableFrom(InRangeRestrictionDto.class)) {
            return new InRangeRestriction();
        } else if (restrictionDtoClass.isAssignableFrom(SimpleValueRestrictionDto.class)) {
            return new SimpleValueRestriction();
        }
        throw new IllegalStateException("Invalid field type: " + restrictionDtoClass);
    }

}

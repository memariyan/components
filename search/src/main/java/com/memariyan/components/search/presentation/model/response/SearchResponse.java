package com.memariyan.components.search.presentation.model.response;

import com.memariyan.components.common.dto.BaseDTO;
import com.memariyan.components.common.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class SearchResponse<T extends BaseDTO> extends BaseResponse {

    private List<T> items;

    private long totalPages;

    private long totalElements;

}

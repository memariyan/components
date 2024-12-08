package com.memariyan.components.common.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public abstract class BaseResponse {

    private Result result;

    public BaseResponse() {
    }

    public BaseResponse(Integer errorCode, String errorMessage) {
        this(errorCode, List.of(errorMessage));
    }

    public BaseResponse(Integer errorCode, List<String> errorMessages) {
        this.result = new Result();
        this.result.setLevel(ResultLevel.BLOCKER);
        this.result.setErrorCode(errorCode);
        this.result.setErrorMessages(errorMessages);
    }
}

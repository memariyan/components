package com.memariyan.components.common.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class ErrorResponse extends BaseResponse {

    public ErrorResponse(Integer errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }

    public ErrorResponse(Integer errorCode, List<String> errorMessages) {
        super(errorCode, errorMessages);
    }

}

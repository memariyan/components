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
public class Result {

    private ResultLevel level;

    private Integer errorCode;

    private List<String> errorMessages;

}

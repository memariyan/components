package com.memariyan.components.user.presentation.api.model.response;

import com.memariyan.components.common.response.BaseResponse;
import com.memariyan.components.user.domain.enums.UserStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class BaseUserResponse extends BaseResponse {

    private String userId;

    private UserStatus userStatus;

    private String email;

}

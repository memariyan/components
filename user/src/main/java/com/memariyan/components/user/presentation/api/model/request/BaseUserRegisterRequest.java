package com.memariyan.components.user.presentation.api.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.memariyan.components.common.annotation.Password;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString(callSuper = true)
public abstract class BaseUserRegisterRequest extends BaseUserInfoRequest {

    @JsonProperty("parentBusinessId")
    protected String parentUserId;

    protected String username;

    @Password(message = "password.is.weak")
    protected String password;

}

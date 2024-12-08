package com.memariyan.components.user.presentation.api.model.request;

import com.memariyan.components.common.annotation.Email;
import com.memariyan.components.common.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString(callSuper = true)
public abstract class BaseUserInfoRequest extends BaseRequest {

    @Email(message = "email.not.valid")
    protected String email;

    protected String externalId;

}

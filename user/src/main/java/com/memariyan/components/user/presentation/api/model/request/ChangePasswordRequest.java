package com.memariyan.components.user.presentation.api.model.request;

import com.memariyan.components.common.annotation.Password;
import com.memariyan.components.common.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChangePasswordRequest extends BaseRequest {

    @Password(message = "password.is.weak")
    private String password;

    @NotBlank(message = "confirm.password.not.valid")
    private  String confirmPassword;

}

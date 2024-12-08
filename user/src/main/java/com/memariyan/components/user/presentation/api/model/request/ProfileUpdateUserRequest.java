package com.memariyan.components.user.presentation.api.model.request;

import com.memariyan.components.common.annotation.Email;
import com.memariyan.components.common.request.BaseRequest;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProfileUpdateUserRequest extends BaseRequest {

    @Size(min = 7, max = 50, message = "name.size")
    private String name;

    @Email(message = "email.not.valid")
    private String email;

}

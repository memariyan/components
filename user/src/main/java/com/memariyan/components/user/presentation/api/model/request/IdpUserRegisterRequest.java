package com.memariyan.components.user.presentation.api.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class IdpUserRegisterRequest {
    private String id;
    private String firstName;
    private String lastName;
    private String password;
    private String confirmPassword;
    private String email;
    private String username;
    private boolean enable;
    private String roles;
}

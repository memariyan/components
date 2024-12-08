package com.memariyan.components.user.service.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChangePasswordModel {

    private String userId;

    private String password;

    private  String confirmPassword;

}

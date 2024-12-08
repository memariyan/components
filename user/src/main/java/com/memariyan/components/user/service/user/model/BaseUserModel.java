package com.memariyan.components.user.service.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public abstract class BaseUserModel {

    private String userId;

    private String parentUserId;

    private String username;

    private String password;

    private String name;

    private String email;

    private Set<String> roles;

}

package com.memariyan.components.user.service.idp.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class IdpChangePasswordModel {

    private String  username;

    private String password;

}

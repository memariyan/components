package com.memariyan.components.user.service.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class UserRegisterResult {

    private String id;

    private String tenantId;

    private String idpUserId;

    private boolean alreadyRegistered;

}

package com.memariyan.components.user.service.idp.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class IdpUserRegisterResult {

    private String userId;

    private boolean alreadyRegistered;

}

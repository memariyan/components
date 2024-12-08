package com.memariyan.components.user.service.account.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class AccountModel {

    private String username;

    private String parentUserId;

    private String type;

    private String externalId;

}

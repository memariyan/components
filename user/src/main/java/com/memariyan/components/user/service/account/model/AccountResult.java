package com.memariyan.components.user.service.account.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class AccountResult {

    private String  id;

    private String tenantId;

}

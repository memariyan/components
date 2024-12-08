package com.memariyan.components.user.service.user.model;

import com.memariyan.components.user.domain.enums.UserStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class BaseUserResult {

    private Long id;

    private String tenantId;

    private String externalId;

    private String userId;

    private UserStatus userStatus;

    private String name;

    private String email;

}

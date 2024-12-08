package com.memariyan.components.user.service.idp.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class IdentityResponse {
    private String userId;
    private String id;
    private String token;
    private String refreshToken;
    private String scope;
    private String tokenType;
    private Long expiredIn;
    private Long refreshExpiredIn;
    private Map<String, Object> claims;
}

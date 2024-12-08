package com.memariyan.components.user.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "com.memariyan.components.user.keycloak")
public class KeycloakProperties {

    private String url;

    private String realm;

    private String adminRealm;

    private String adminUsername;

    private String adminPassword;

    private String adminClientId;

}

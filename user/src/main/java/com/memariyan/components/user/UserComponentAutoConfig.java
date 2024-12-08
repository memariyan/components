package com.memariyan.components.user;

import com.memariyan.components.user.config.KeycloakProperties;
import com.memariyan.components.user.service.idp.impl.KeycloakIdentityProvider;
import org.keycloak.admin.client.Keycloak;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConfigurationPropertiesScan
@EnableConfigurationProperties
public class UserComponentAutoConfig {

    @Bean
    @ConditionalOnMissingBean
    public Keycloak keycloak(KeycloakProperties keycloakProperties) {
        return Keycloak.getInstance(keycloakProperties.getUrl(), keycloakProperties.getAdminRealm(),
                keycloakProperties.getAdminUsername(), keycloakProperties.getAdminPassword(), keycloakProperties.getAdminClientId());
    }

    @Bean
    @ConditionalOnMissingBean
    public KeycloakIdentityProvider keycloakIdentityProvider(Keycloak keycloak, KeycloakProperties keycloakProperties) {
        return new KeycloakIdentityProvider(keycloak, keycloakProperties);
    }

}

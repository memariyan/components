package com.memariyan.components.user.service.idp.impl;

import com.memariyan.components.common.exception.ClientException;
import com.memariyan.components.common.exception.NotFoundException;
import com.memariyan.components.common.exception.ValidationException;
import com.memariyan.components.user.config.KeycloakProperties;
import com.memariyan.components.user.service.idp.IdentityProvider;
import com.memariyan.components.user.service.idp.model.request.IdpChangePasswordModel;
import com.memariyan.components.user.service.idp.model.request.IdpUserRegisterModel;
import com.memariyan.components.user.service.idp.model.response.IdpUserRegisterResult;
import jakarta.ws.rs.core.Response;
import org.apache.commons.collections4.CollectionUtils;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;

import java.util.*;
import java.util.stream.Collectors;

public class KeycloakIdentityProvider implements IdentityProvider {

    private final Keycloak keycloak;
    private final KeycloakProperties keycloakProperties;

    public KeycloakIdentityProvider(Keycloak keycloak, KeycloakProperties keycloakProperties) {
        this.keycloak = keycloak;
        this.keycloakProperties = keycloakProperties;
    }

    @Override
    public IdpUserRegisterResult registerUser(IdpUserRegisterModel request) {
        UserRepresentation user = getUserRepresentation(request);
        Response response = keycloak.realm(keycloakProperties.getRealm()).users().create(user);
        if (HttpStatus.CONFLICT.value() == response.getStatusInfo().getStatusCode()) {
            return findUser(request.getUsername())
                    .map(u -> new IdpUserRegisterResult().setAlreadyRegistered(true).setUserId(u.getId())).get();

        } else if (HttpStatus.CREATED.value() != response.getStatusInfo().getStatusCode()) {
            throw new ClientException(response.getStatusInfo().getReasonPhrase(), HttpStatus.valueOf(response.getStatus()));

        } else if (Objects.isNull(response.getLocation())) {
            throw new ValidationException("keycloak create user response is not valid. response: " + response);
        }
        String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
        return new IdpUserRegisterResult().setAlreadyRegistered(false).setUserId(userId);
    }

    @Override
    public void addRoles(String userId, Set<String> inputRoles) {
        List<RoleRepresentation> currentRoles = getUserRoles(userId);
        Set<String> newRoleNames = new HashSet<>(inputRoles);
        newRoleNames.removeAll(currentRoles.stream().map(RoleRepresentation::getName).collect(Collectors.toSet()));

        if (CollectionUtils.isEmpty(newRoleNames)) {
            return;
        }
        keycloak.realm(keycloakProperties.getRealm()).users().get(userId).roles().realmLevel().add(
                getRolesByName(newRoleNames)
        );
    }

    private List<RoleRepresentation> getRolesByName(Set<String> roleNames) {
        return keycloak.realm(keycloakProperties.getRealm()).roles().list().stream()
                .filter(role -> roleNames.contains(role.getName()))
                .collect(Collectors.toList());
    }

    private List<RoleRepresentation> getUserRoles(String userId) {
        return keycloak.realm(keycloakProperties.getRealm()).users().get(userId).roles().realmLevel().listAll();
    }

    private Optional<UserRepresentation> findUser(String username) {
        List<UserRepresentation> users = keycloak.realm(keycloakProperties.getRealm()).users().search(username, true);
        if (CollectionUtils.isEmpty(users)) {
            return Optional.empty();
        }
        return Optional.of(users.getFirst());
    }

    private UserRepresentation getUser(String username) {
        return findUser(username).orElseThrow(() -> new NotFoundException("idp.user.not.found"));
    }

    @Override
    public void changePassword(IdpChangePasswordModel request) {
        var credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(request.getPassword());
        credential.setTemporary(false);

        UserRepresentation representation = getUser(request.getUsername());
        UserResource user = keycloak.realm(keycloakProperties.getRealm()).users().get(representation.getId());

        user.resetPassword(credential);
    }

    @Override
    public void accountStatus(String username, Boolean status) {
        var updateUser = new UserRepresentation();
        updateUser.setEnabled(status);

        UserRepresentation representation = getUser(username);
        UserResource user = keycloak.realm(keycloakProperties.getRealm()).users().get(representation.getId());

        user.update(updateUser);
    }

    private static UserRepresentation getUserRepresentation(IdpUserRegisterModel request) {
        var credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(request.getPassword());
        credential.setTemporary(false);

        var user = new UserRepresentation();
        user.setUsername(request.getUsername());
        user.setEnabled(true);
        user.setCredentials(Collections.singletonList(credential));
        return user;
    }
}

package com.memariyan.components.user.service.idp;

import com.memariyan.components.user.service.idp.model.request.IdpChangePasswordModel;
import com.memariyan.components.user.service.idp.model.request.IdpUserRegisterModel;
import com.memariyan.components.user.service.idp.model.response.IdpUserRegisterResult;

import java.util.Set;

public interface IdentityProvider {

    IdpUserRegisterResult registerUser(IdpUserRegisterModel request);

    void changePassword(IdpChangePasswordModel request);

    void accountStatus(String username, Boolean status);

    void addRoles(String userId, Set<String> roleNames);

}

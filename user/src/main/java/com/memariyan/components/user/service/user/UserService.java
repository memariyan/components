package com.memariyan.components.user.service.user;

import com.memariyan.components.user.service.user.model.BaseUserModel;
import com.memariyan.components.user.service.user.model.BaseUserResult;
import com.memariyan.components.user.service.user.model.ChangePasswordModel;

import java.util.Set;

public interface UserService<TServiceModel extends BaseUserModel, TServiceResult extends BaseUserResult> {

    TServiceResult register(TServiceModel entity);

    TServiceResult updateProfile(TServiceModel request);

    void changePassword(ChangePasswordModel request);

    void block(String userId);

    void unblock(String userId);

    TServiceResult getUser(String userId);

    void delete(String userId);

    void recover(String userId);

    String getType();

    Set<String> getManageableParentUsers();

}
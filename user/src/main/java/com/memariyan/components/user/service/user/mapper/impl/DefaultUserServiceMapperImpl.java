package com.memariyan.components.user.service.user.mapper.impl;

import com.memariyan.components.user.domain.BaseUser;
import com.memariyan.components.user.service.account.model.AccountResult;
import com.memariyan.components.user.service.idp.model.request.IdpChangePasswordModel;
import com.memariyan.components.user.service.idp.model.response.IdpUserRegisterResult;
import com.memariyan.components.user.service.user.mapper.UserServiceMapper;
import com.memariyan.components.user.service.user.model.BaseUserModel;
import com.memariyan.components.user.service.user.model.BaseUserResult;
import com.memariyan.components.user.service.user.model.ChangePasswordModel;
import com.memariyan.components.user.service.user.model.UserRegisterResult;


public abstract class DefaultUserServiceMapperImpl<TEntity extends BaseUser, TServiceModel extends BaseUserModel,
        TServiceResult extends BaseUserResult>
        implements UserServiceMapper<TEntity, TServiceModel, TServiceResult> {

    @Override
    public IdpChangePasswordModel toIdpChangePasswordModel(ChangePasswordModel model, String username) {
        return new IdpChangePasswordModel()
                .setUsername(username)
                .setPassword(model.getPassword());
    }

    @Override
    public UserRegisterResult toResult(AccountResult accountResult, IdpUserRegisterResult idpRegisterResult) {
        UserRegisterResult result = new UserRegisterResult();
        result.setId(accountResult.getId());
        result.setTenantId(accountResult.getTenantId());
        result.setIdpUserId(idpRegisterResult.getUserId());
        result.setAlreadyRegistered(idpRegisterResult.isAlreadyRegistered());
        return result;
    }
}

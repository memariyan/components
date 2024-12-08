package com.memariyan.components.user.service.user.mapper;


import com.memariyan.components.user.domain.BaseUser;
import com.memariyan.components.user.service.idp.model.request.IdpChangePasswordModel;
import com.memariyan.components.user.service.idp.model.request.IdpUserRegisterModel;
import com.memariyan.components.user.service.account.model.AccountModel;
import com.memariyan.components.user.service.account.model.AccountResult;
import com.memariyan.components.user.service.idp.model.response.IdpUserRegisterResult;
import com.memariyan.components.user.service.user.model.UserRegisterResult;
import com.memariyan.components.user.service.user.model.BaseUserModel;
import com.memariyan.components.user.service.user.model.BaseUserResult;
import com.memariyan.components.user.service.user.model.ChangePasswordModel;

public interface UserServiceMapper<TEntity extends BaseUser, TServiceModel extends BaseUserModel, TServiceResult extends BaseUserResult> {

    IdpUserRegisterModel toIdpRegisterModel(TServiceModel model);

    IdpChangePasswordModel toIdpChangePasswordModel(ChangePasswordModel model, String username);

    TServiceResult toResult(TEntity entity);

    TEntity toEntity(TServiceModel model);

    AccountModel toAccountModel(TServiceModel model, String type);

    UserRegisterResult toResult(AccountResult result, IdpUserRegisterResult idpRegisterResult);
}

package com.memariyan.components.user.presentation.api.mapper.impl;

import com.memariyan.components.user.presentation.api.mapper.BaseUserControllerMapper;
import com.memariyan.components.user.presentation.api.model.request.BaseUserInfoRequest;
import com.memariyan.components.user.presentation.api.model.request.BaseUserRegisterRequest;
import com.memariyan.components.user.presentation.api.model.request.ChangePasswordRequest;
import com.memariyan.components.user.presentation.api.model.response.BaseUserResponse;
import com.memariyan.components.user.service.user.model.BaseUserModel;
import com.memariyan.components.user.service.user.model.BaseUserResult;
import com.memariyan.components.user.service.user.model.ChangePasswordModel;

public abstract class DefaultUserControllerMapperImpl<TRequestInfo extends BaseUserInfoRequest,
        TRequest extends BaseUserRegisterRequest,
        TResponse extends BaseUserResponse,
        TServiceModel extends BaseUserModel,
        TServiceResult extends BaseUserResult>
        implements BaseUserControllerMapper<TRequestInfo, TRequest, TResponse, TServiceModel, TServiceResult> {

    @Override
    public ChangePasswordModel toChangePasswordModel(String userId, ChangePasswordRequest request) {
        ChangePasswordModel result = new ChangePasswordModel();
        result.setUserId(userId);
        result.setPassword(request.getPassword());
        result.setConfirmPassword(request.getConfirmPassword());
        return result;
    }

}

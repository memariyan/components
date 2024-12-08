package com.memariyan.components.user.presentation.api.mapper;

import com.memariyan.components.user.presentation.api.model.request.BaseUserInfoRequest;
import com.memariyan.components.user.presentation.api.model.request.BaseUserRegisterRequest;
import com.memariyan.components.user.presentation.api.model.request.ChangePasswordRequest;
import com.memariyan.components.user.presentation.api.model.response.BaseUserResponse;
import com.memariyan.components.user.service.user.model.BaseUserModel;
import com.memariyan.components.user.service.user.model.BaseUserResult;
import com.memariyan.components.user.service.user.model.ChangePasswordModel;

public interface BaseUserControllerMapper<TRequestInfo extends BaseUserInfoRequest, TRequest extends BaseUserRegisterRequest,
        TResponse extends BaseUserResponse, TServiceModel extends BaseUserModel, TServiceResult extends BaseUserResult> {

    TServiceModel toModel(TRequest request);

    TResponse toResponse(TServiceResult entity);

    ChangePasswordModel toChangePasswordModel(String userId, ChangePasswordRequest request);

    TServiceModel toModel(String userId, TRequestInfo request);


}


package com.memariyan.components.user.presentation.api;

import com.memariyan.components.common.model.HeaderNames;
import com.memariyan.components.user.presentation.api.mapper.BaseUserControllerMapper;
import com.memariyan.components.user.presentation.api.model.request.BaseUserInfoRequest;
import com.memariyan.components.user.presentation.api.model.request.BaseUserRegisterRequest;
import com.memariyan.components.user.presentation.api.model.response.BaseUserResponse;
import com.memariyan.components.user.service.user.UserService;
import com.memariyan.components.user.service.user.model.BaseUserModel;
import com.memariyan.components.user.service.user.model.BaseUserResult;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface BaseUserController<
        TRequestInfo extends BaseUserInfoRequest,
        TRequest extends BaseUserRegisterRequest,
        TResponse extends BaseUserResponse,
        TServiceModel extends BaseUserModel,
        TServiceResult extends BaseUserResult> {

    UserService<TServiceModel, TServiceResult> getUserService();

    BaseUserControllerMapper<TRequestInfo, TRequest, TResponse, TServiceModel, TServiceResult> getUserMapper();

    @GetMapping("/profile")
    default ResponseEntity<?> getProfile(@RequestHeader(value = HeaderNames.USER_ID) String userId) {
        TServiceResult entity = getUserService().getUser(userId);
        var response = getUserMapper().toResponse(entity);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/profile")
    default ResponseEntity<?> updateProfile(@RequestHeader(value = HeaderNames.USER_ID) String userId,
                                            @Valid @RequestBody TRequestInfo request) {
        TServiceResult entity = getUserService().updateProfile(getUserMapper().toModel(userId, request));
        var response = getUserMapper().toResponse(entity);
        return ResponseEntity.ok(response);
    }

}

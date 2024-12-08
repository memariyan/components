package com.memariyan.components.user.presentation.api;

import com.memariyan.components.common.model.HeaderNames;
import com.memariyan.components.user.presentation.api.mapper.BaseUserControllerMapper;
import com.memariyan.components.user.presentation.api.model.request.BaseUserInfoRequest;
import com.memariyan.components.user.presentation.api.model.request.BaseUserRegisterRequest;
import com.memariyan.components.user.presentation.api.model.request.ChangePasswordRequest;
import com.memariyan.components.user.presentation.api.model.response.BaseUserResponse;
import com.memariyan.components.user.service.user.UserService;
import com.memariyan.components.user.service.user.model.BaseUserModel;
import com.memariyan.components.user.service.user.model.BaseUserResult;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface BaseUserAdminController<
        TRequestInfo extends BaseUserInfoRequest,
        TRequest extends BaseUserRegisterRequest,
        TResponse extends BaseUserResponse,
        TServiceModel extends BaseUserModel,
        TServiceResult extends BaseUserResult> {

    UserService<TServiceModel, TServiceResult> getUserService();

    BaseUserControllerMapper<TRequestInfo, TRequest, TResponse, TServiceModel, TServiceResult> getUserMapper();

    @PostMapping
    default ResponseEntity<?> register(
            @RequestHeader(value = HeaderNames.USER_ID) String managerUserId,
            @Valid @RequestBody TRequest request) {
        TServiceResult entity = getUserService().register(getUserMapper().toModel(request));
        var response = getUserMapper().toResponse(entity);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{userId}/change-password")
    default ResponseEntity<Void> changePassword(@PathVariable("userId") String userId, @Valid @RequestBody ChangePasswordRequest request) {
        getUserService().changePassword(getUserMapper().toChangePasswordModel(userId, request));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}/block")
    default ResponseEntity<Void> block(@PathVariable("userId") String userId) {
        getUserService().block(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}/unblock")
    default ResponseEntity<Void> unblock(@PathVariable("userId") String userId) {
        getUserService().unblock(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}")
    default ResponseEntity<?> updateProfile(@PathVariable("userId") String userId, @Valid @RequestBody TRequestInfo request) {
        TServiceResult entity = getUserService().updateProfile(getUserMapper().toModel(userId, request));
        var response = getUserMapper().toResponse(entity);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    default ResponseEntity<?> getUserById(@PathVariable("userId") String id) {
        TServiceResult entity = getUserService().getUser(id);
        var response = getUserMapper().toResponse(entity);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}")
    default ResponseEntity<Void> deleteByUserId(@PathVariable("userId") String id) {
        getUserService().delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}/recover")
    default ResponseEntity<Void> recoverByUserId(@PathVariable("userId") String id) {
        getUserService().recover(id);
        return ResponseEntity.noContent().build();
    }
}

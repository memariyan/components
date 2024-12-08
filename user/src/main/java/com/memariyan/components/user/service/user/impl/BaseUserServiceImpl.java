package com.memariyan.components.user.service.user.impl;

import com.memariyan.components.common.exception.DuplicateException;
import com.memariyan.components.common.exception.NotFoundException;
import com.memariyan.components.common.exception.UserHasBeenDeletedException;
import com.memariyan.components.common.exception.ValidationException;
import com.memariyan.components.user.service.idp.IdentityProvider;
import com.memariyan.components.user.domain.BaseUserRepository;
import com.memariyan.components.user.domain.BaseUser;
import com.memariyan.components.user.domain.enums.UserStatus;
import com.memariyan.components.user.service.idp.model.response.IdpUserRegisterResult;
import com.memariyan.components.user.service.account.AccountService;
import com.memariyan.components.user.service.account.model.AccountResult;
import com.memariyan.components.user.service.user.model.UserRegisterResult;
import com.memariyan.components.user.service.user.mapper.UserServiceMapper;
import com.memariyan.components.user.service.user.UserService;
import com.memariyan.components.user.service.user.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Slf4j
public abstract class BaseUserServiceImpl<TEntity extends BaseUser, TServiceModel extends BaseUserModel,
        TServiceResult extends BaseUserResult>
        implements UserService<TServiceModel, TServiceResult> {

    private final UserServiceMapper<TEntity, TServiceModel, TServiceResult> mapper;

    private final BaseUserRepository<TEntity> repository;

    private final IdentityProvider identityProvider;

    private final AccountService accountService;

    public BaseUserServiceImpl(
            UserServiceMapper<TEntity, TServiceModel, TServiceResult> mapper,
            BaseUserRepository<TEntity> repository,
            IdentityProvider identityProvider, AccountService accountService) {

        this.mapper = mapper;
        this.repository = repository;
        this.identityProvider = identityProvider;
        this.accountService = accountService;
    }

    protected void beforeRegister(TEntity entity, TServiceModel request) {

    }

    protected void afterRegister(TEntity entity) {

    }

    @Override
    @Transactional
    public TServiceResult register(TServiceModel request) {
        log.debug("creating user with request : {}", request);

        TEntity entity = mapper.toEntity(request);
        beforeRegister(entity, request);
        UserRegisterResult registerResult = registerUser(request, getType());
        applyRegisterResult(registerResult, entity);
        registerRoles(registerResult.getIdpUserId(), request.getRoles());
        TEntity savedEntity = save(entity);
        afterRegister(savedEntity);

        log.debug("user created : {}", entity);
        return mapper.toResult(entity);
    }

    private UserRegisterResult registerUser(TServiceModel model, String type) {
        IdpUserRegisterResult registerResult = identityProvider.registerUser(mapper.toIdpRegisterModel(model));
        AccountResult serviceResult = accountService.createUser(mapper.toAccountModel(model, type));
        return mapper.toResult(serviceResult, registerResult);
    }

    private void registerRoles(String idpUserId, Set<String> roles) {
        identityProvider.addRoles(idpUserId, roles);
    }

    protected void applyRegisterResult(UserRegisterResult result, TEntity entity) {
        entity.setUserId(result.getId());
        entity.setTenantId(result.getTenantId());
        if (!result.isAlreadyRegistered()) {
            return;
        }
        isUserExist(entity)
                .filter(userExistenceResult -> StringUtils.isNoneBlank(userExistenceResult.getUserId()))
                .ifPresent(userExistenceResult -> {
                    if (Boolean.TRUE.equals(userExistenceResult.isDeleted())) {
                        throw new UserHasBeenDeletedException("user.deleted.before");
                    }
                    throw new DuplicateException("duplicate.user.exist");
                });
    }

    @Override
    public void changePassword(ChangePasswordModel request) {
        TEntity user = getByUserId(request.getUserId());
        log.debug("going to change password with request:{} and user: {}", request, user);
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new ValidationException("password.and.confirm.are.not.match");
        }
        var username = getUsernameById(request.getUserId());
        identityProvider.changePassword(mapper.toIdpChangePasswordModel(request, username));
    }

    @Override
    public void block(String userId) {
        log.debug("going to block user by id :{}", userId);
        TEntity user = getByUserId(userId);
        changeStatus(userId, UserStatus.BLOCKED);
        user.setUserStatus(UserStatus.BLOCKED);
        save(user);
    }

    @Override
    public void unblock(String userId) {
        log.debug("going to unblock user by id :{}", userId);
        TEntity user = getByUserId(userId);
        changeStatus(userId, UserStatus.ACTIVE);
        user.setUserStatus(UserStatus.ACTIVE);
        save(user);
    }

    private void changeStatus(String userId, UserStatus status) {
        var username = getUsernameById(userId);
        identityProvider.accountStatus(username, !UserStatus.BLOCKED.equals(status));
        accountService.changeStatus(userId, status);
    }

    @Override
    public TServiceResult updateProfile(TServiceModel request) {
        TEntity entity = getByUserId(request.getUserId());
        updateProfile(entity, request);
        TEntity updatedEntity = save(entity);
        return mapper.toResult(updatedEntity);
    }

    protected void updateProfile(TEntity entity, TServiceModel request) {
        entity.setEmail(request.getEmail());
    }

    @Override
    public TServiceResult getUser(String userId) {
        return mapper.toResult(getByUserId(userId));
    }

    @Override
    public void delete(String userId) {
        TEntity user = getByUserId(userId);
        user.setDeleted(true);
        repository.save(user);
    }

    @Override
    public void recover(String userId) {
        TEntity user = repository.getByUserId(userId);
        user.setDeleted(false);
        identityProvider.accountStatus(getUsernameById(userId), true);
        repository.save(user);
    }

    protected TEntity getByUserId(String userId) {
        return repository.findByUserId(userId).orElseThrow(() -> new NotFoundException("entity.not.found"));
    }

    public String getUsernameById(String userId) {
        return accountService.getUsernameById(userId);
    }

    protected abstract Optional<UserExistenceResult> isUserExist(TEntity entity);

    protected TEntity save(TEntity entity) {
        return repository.save(entity);
    }

    @Override
    public Set<String> getManageableParentUsers() {
        return Set.of();
    }
}

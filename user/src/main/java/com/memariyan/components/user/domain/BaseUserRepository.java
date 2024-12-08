package com.memariyan.components.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BaseUserRepository<TEntity extends BaseUser> extends JpaRepository<TEntity, Long> {

    Optional<TEntity> findByUserId(String userId);

    TEntity getByUserId(String userId);
}

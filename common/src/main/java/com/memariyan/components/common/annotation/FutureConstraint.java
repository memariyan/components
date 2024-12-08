package com.memariyan.components.common.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;
import java.util.Objects;

public class FutureConstraint implements ConstraintValidator<Future, LocalDateTime> {
    @Override
    public boolean isValid(LocalDateTime time, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(time)) return true;
        return !LocalDateTime.now().isAfter(time);
    }
}

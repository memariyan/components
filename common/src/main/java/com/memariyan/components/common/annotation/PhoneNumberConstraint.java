package com.memariyan.components.common.annotation;

import com.memariyan.components.common.model.Patterns;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class PhoneNumberConstraint implements ConstraintValidator<PhoneNumber, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(s)) return true;
        return Patterns.isValidPhoneNumber(s);
    }
}

package com.memariyan.components.common.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordConstraint.class)
public @interface Password {
    String message() default  "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

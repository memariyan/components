package com.memariyan.components.common.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
@Constraint(validatedBy = FutureConstraint.class)
@Documented
public @interface Future {
    String message() default "invalid.time.input";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

package com.memariyan.components.common.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = FileFormatConstraint.class)
public @interface FileFormat {
    String[] formats() default {};
    int minLength() default 0;
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

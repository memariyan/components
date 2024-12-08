package com.memariyan.components.common.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;
import java.util.stream.IntStream;

public class NationalCodeConstraint implements ConstraintValidator<NationalCode, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(s)) return true;
        return check(s);
    }

    private boolean check(String input) {
        if (!input.matches("^\\d{10}$"))
            return false;

        int check = Integer.parseInt(input.substring(9, 10));

        int sum = IntStream.range(0, 9)
                .map(x -> Integer.parseInt(input.substring(x, x + 1)) * (10 - x))
                .sum() % 11;

        return sum < 2 ? check == sum : check + sum == 11;
    }
}

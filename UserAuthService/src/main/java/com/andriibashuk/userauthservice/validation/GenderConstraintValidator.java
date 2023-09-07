package com.andriibashuk.userauthservice.validation;

import com.andriibashuk.userauthservice.entity.User;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class GenderConstraintValidator implements ConstraintValidator<GenderConstraint, Object> {
    private User.Gender[] array;
    @Override
    public void initialize(GenderConstraint constraintAnnotation) {
        this.array = constraintAnnotation.array();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        return Arrays.stream(array).map(Enum::name).anyMatch(item -> item.equals(value));
    }
}

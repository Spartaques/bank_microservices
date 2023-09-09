package com.andriibashuk.userauthservice.validation;

import com.andriibashuk.userauthservice.entity.PhoneNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class PhoneNumberValidValidator implements ConstraintValidator<PhoneNumbersValid, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if(value == null) {
            return false;
        }
        if(value instanceof Set<?> && !((Set<?>) value).isEmpty()) {
            return ((Set<?>) value).stream().map(item -> (PhoneNumber) item).allMatch(item -> item.getNumber() != null);
        }

        return false;
    }
}

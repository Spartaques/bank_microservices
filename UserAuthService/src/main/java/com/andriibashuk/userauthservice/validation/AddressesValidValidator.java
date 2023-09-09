package com.andriibashuk.userauthservice.validation;

import com.andriibashuk.userauthservice.entity.Address;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AddressesValidValidator implements ConstraintValidator<AddressesValid, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value instanceof Set<?> && !((Set<?>) value).isEmpty()) {
            return ((Set<?>) value).stream().map(item -> (Address) item).allMatch(address -> address.getCity() != null && address.getState() != null && address.getStreet() != null);
        }

        return false;
    }
}

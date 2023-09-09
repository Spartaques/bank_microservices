package com.andriibashuk.userauthservice.validation;

import com.andriibashuk.userauthservice.entity.Address;
import com.andriibashuk.userauthservice.entity.Audit;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Collections;
import java.util.Set;

public class AuditValidValidator implements ConstraintValidator<AuditsValid, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if(value == null) {
            return false;
        }
        if(value instanceof Set<?> && !((Set<?>) value).isEmpty()) {
            return ((Set<?>) value).stream().map(item -> (Audit) item).allMatch(item -> item.getEvent() != null);
        }

        return false;
    }
}

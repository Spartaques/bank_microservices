package com.andriibashuk.userauthservice.validation;

import com.andriibashuk.userauthservice.entity.User;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = GenderConstraintValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface GenderConstraint {
    User.Gender[] array();
    String message() default "Invalid gender Should be one of {array}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
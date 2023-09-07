package com.andriibashuk.userauthservice.validation;

import com.andriibashuk.userauthservice.entity.Address;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;
import java.util.List;

@Documented
@Constraint(validatedBy = AddressesValidValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AddressesValid {
    String message() default "Invalid addresses {addresses}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

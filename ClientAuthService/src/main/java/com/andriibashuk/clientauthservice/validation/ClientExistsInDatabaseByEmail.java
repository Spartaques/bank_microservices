package com.andriibashuk.clientauthservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = ClientExistsInDatabaseByEmailValidator.class)
@Documented
public @interface ClientExistsInDatabaseByEmail {
    String message() default "Client already exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

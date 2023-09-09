package com.andriibashuk.userauthservice.validation;

import com.andriibashuk.userauthservice.entity.User;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = AuditValidValidator.class)
@Documented
public @interface AuditsValid {
    String message() default "Invalid audits";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

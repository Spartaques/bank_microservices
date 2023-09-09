package com.andriibashuk.userauthservice.validation;

import com.andriibashuk.userauthservice.entity.Audit;
import com.andriibashuk.userauthservice.entity.Comment;
import com.andriibashuk.userauthservice.entity.UserComment;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class CommentsValidValidator implements ConstraintValidator<CommentsValid, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if(value == null) {
            return false;
        }
        if(value instanceof Set<?> && !((Set<?>) value).isEmpty()) {
            return ((Set<?>) value).stream().map(item -> (UserComment) item).allMatch(item -> item.getText() != null && item.getEntityId() != 0);
        }

        return false;
    }
}

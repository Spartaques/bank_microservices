package com.andriibashuk.userauthservice.validation;

import com.andriibashuk.userauthservice.entity.User;
import com.andriibashuk.userauthservice.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserExistsInDatabaseByEmailValidator implements ConstraintValidator<UserExistsInDatabaseByEmail, String> {
    private final UserRepository userRepository;

    public UserExistsInDatabaseByEmailValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void initialize(UserExistsInDatabaseByEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Optional<User> byEmail = this.userRepository.findByEmail(value);
        return byEmail.isEmpty();
    }
}

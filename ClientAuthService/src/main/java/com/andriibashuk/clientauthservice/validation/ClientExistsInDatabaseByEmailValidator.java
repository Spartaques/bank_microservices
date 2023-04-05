package com.andriibashuk.clientauthservice.validation;

import com.andriibashuk.clientauthservice.entity.Client;
import com.andriibashuk.clientauthservice.repository.ClientRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientExistsInDatabaseByEmailValidator implements ConstraintValidator<ClientExistsInDatabaseByEmail, String> {
    private final ClientRepository clientRepository;

    public ClientExistsInDatabaseByEmailValidator(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void initialize(ClientExistsInDatabaseByEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Optional<Client> byEmail = this.clientRepository.findByEmail(value);
        return byEmail.isEmpty();
    }
}

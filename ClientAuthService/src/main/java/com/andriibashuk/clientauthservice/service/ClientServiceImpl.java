package com.andriibashuk.clientauthservice.service;

import com.andriibashuk.clientauthservice.entity.Client;
import com.andriibashuk.clientauthservice.exception.ClientNotFoundException;
import com.andriibashuk.clientauthservice.repository.ClientRepository;
import com.andriibashuk.clientauthservice.response.ClientResponse;
import com.andriibashuk.clientauthservice.security.JWTUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log
public class ClientServiceImpl implements ClientService {
    PasswordEncoder passwordEncoder;

    ClientRepository clientRepository;

    JWTUtil jwtUtil;

    public ClientServiceImpl(PasswordEncoder passwordEncoder, ClientRepository clientRepository, JWTUtil jwtUtil) {
        this.passwordEncoder = passwordEncoder;
        this.clientRepository = clientRepository;
        this.jwtUtil = jwtUtil;
    }
    @Override
    public ClientResponse register(String firstName, String lastName, String email, String password, Short age, Client.Gender gender) {
        log.info("Starting registering client");
        Client client = new Client();
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setEmail(email);
        client.setAge(age);
        client.setGender(gender);
        client.setPassword(passwordEncoder.encode(password));
        clientRepository.save(client);
        log.info("Client created in database");

        return ClientResponse
                .builder()
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .email(client.getEmail())
                .age(client.getAge())
                .gender(client.getGender())
                .createdDate(client.getCreatedDate())
                .lastModifiedDate(client.getLastModifiedDate())
                .lastModifiedBy(client.getLastModifiedBy())
                .createdBy(client.getCreatedBy()).build();
    }

    @Override
    public String login(String email, String password) {
        Optional<Client> byEmail = clientRepository.findByEmail(email);
        if(byEmail.isEmpty()) {
            throw new ClientNotFoundException("client not found", HttpStatus.NOT_FOUND, "CLIENT_NOT_FOUND");
        }
        Client client = byEmail.get();

        if(!passwordEncoder.matches(password, client.getPassword())) {
            throw new ClientNotFoundException("client not found", HttpStatus.NOT_FOUND, "CLIENT_NOT_FOUND");
        }

        return jwtUtil.generateToken(client);

    }
}

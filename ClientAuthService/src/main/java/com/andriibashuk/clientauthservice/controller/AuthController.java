package com.andriibashuk.clientauthservice.controller;

import com.andriibashuk.clientauthservice.request.LoginRequest;
import com.andriibashuk.clientauthservice.request.RegisterClientRequest;
import com.andriibashuk.clientauthservice.response.ClientResponse;
import com.andriibashuk.clientauthservice.service.ClientService;
import jakarta.validation.Valid;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@Log
public class AuthController {
    private final ClientService clientService;

    public AuthController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/auth/client/hello")
    public String hello() {
        return "hello";
    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginRequest request) {
        log.info("Request for login client with credentials"+request);
        return clientService.login(request.getEmail(), request.getPassword());
    }

    @PostMapping("/register")
    public ResponseEntity<ClientResponse> register(@Valid @RequestBody RegisterClientRequest request) {
        log.info("Request for register client with data"+request);
        return new ResponseEntity<>(this.clientService.register(request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPhone(),
                request.getPassword(),
                request.getAge(),
                request.getGender()),
                HttpStatus.OK);
    }

    @GetMapping("/show/{id}")
    public ResponseEntity<ClientResponse> show(@PathVariable("id") Long id ) {
        return new ResponseEntity<>(clientService.getById(id), HttpStatus.OK);
    }
}

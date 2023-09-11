package com.andriibashuk.userauthservice.controller;

import com.andriibashuk.userauthservice.entity.User;
import com.andriibashuk.userauthservice.request.LoginRequest;
import com.andriibashuk.userauthservice.request.RegisterUserRequest;
import com.andriibashuk.userauthservice.request.UsersAllRequest;
import com.andriibashuk.userauthservice.response.UserResponse;
import com.andriibashuk.userauthservice.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginRequest request) {
        log.info("Request for login user with credentials"+request);
        return userService.login(request.getEmail(), request.getPassword());
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterUserRequest request) {
        log.info("Request for register user with data"+request);
        return new ResponseEntity<>(this.userService.register(request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword(),
                request.getAge(),
                User.Gender.valueOf(request.getGender()),
                request.getRolesIds(),
                request.getAddresses(),
                request.getPhoneNumbers(),
                request.getComments(),
                request.getAudits()),
                HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAll(@Valid UsersAllRequest request)
    {
        return new ResponseEntity<>(userService.getAll(request.getId()), HttpStatus.OK);
    }
}

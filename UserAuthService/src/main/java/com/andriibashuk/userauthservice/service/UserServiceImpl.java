package com.andriibashuk.userauthservice.service;

import com.andriibashuk.userauthservice.entity.User;
import com.andriibashuk.userauthservice.exception.UserNotFoundException;
import com.andriibashuk.userauthservice.repository.UserRepository;
import com.andriibashuk.userauthservice.response.UserResponse;
import com.andriibashuk.userauthservice.security.JWTUtil;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log
public class UserServiceImpl implements UserService {
    PasswordEncoder passwordEncoder;

    UserRepository userRepository;

    JWTUtil jwtUtil;

    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, JWTUtil jwtUtil) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }
    @Override
    public UserResponse register(String firstName, String lastName, String email, String password, Short age, User.Gender gender) {
        log.info("Starting registering user");
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setAge(age);
        user.setGender(gender);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        log.info("Client created in database");

        return UserResponse
                .builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .age(user.getAge())
                .gender(user.getGender())
                .createdDate(user.getCreatedDate())
                .lastModifiedDate(user.getLastModifiedDate())
                .lastModifiedBy(user.getLastModifiedBy())
                .createdBy(user.getCreatedBy()).build();
    }

    @Override
    public String login(String email, String password) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        if(byEmail.isEmpty()) {
            throw new UserNotFoundException("user not found", HttpStatus.NOT_FOUND, "USER_NOT_FOUND");
        }
        User user = byEmail.get();

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new UserNotFoundException("user not found", HttpStatus.NOT_FOUND, "USER_NOT_FOUND");
        }

        return jwtUtil.generateToken(user);

    }
}

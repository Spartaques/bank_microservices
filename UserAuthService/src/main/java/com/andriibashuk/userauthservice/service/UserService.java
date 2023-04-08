package com.andriibashuk.userauthservice.service;

import com.andriibashuk.userauthservice.entity.User;
import com.andriibashuk.userauthservice.response.UserResponse;

import java.util.List;
import java.util.Set;

public interface UserService {
    UserResponse register(String firstName, String lastName, String email, String password, Short age, User.Gender gender, List<Long> rolesIds);

    String login(String email, String password);
}

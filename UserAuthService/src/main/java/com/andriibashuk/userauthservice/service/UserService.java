package com.andriibashuk.userauthservice.service;

import com.andriibashuk.userauthservice.entity.*;
import com.andriibashuk.userauthservice.response.UserResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface UserService {
    UserResponse register(String firstName,
                          String lastName,
                          String email,
                          String password,
                          Short age,
                          User.Gender gender,
                          Set<Long> rolesIds,
                          Set<Address> addresses,
                          Set<PhoneNumber> phoneNumbers,
                          Set<UserComment> comments,
                          Set<Audit> audits);

    String login(String email, String password);

    List<UserResponse> getAll(Integer id);
}

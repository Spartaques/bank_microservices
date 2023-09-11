package com.andriibashuk.userauthservice.repository;

import com.andriibashuk.userauthservice.entity.User;
import com.andriibashuk.userauthservice.response.UserResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomUserRepository {
    public List<User> getAll(Integer id);
}

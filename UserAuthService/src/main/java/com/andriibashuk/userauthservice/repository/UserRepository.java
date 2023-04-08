package com.andriibashuk.userauthservice.repository;

import com.andriibashuk.userauthservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String value);
}

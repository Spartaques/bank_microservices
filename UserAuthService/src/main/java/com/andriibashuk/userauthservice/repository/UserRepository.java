package com.andriibashuk.userauthservice.repository;

import com.andriibashuk.userauthservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {
    Optional<User> findByEmail(String value);
}

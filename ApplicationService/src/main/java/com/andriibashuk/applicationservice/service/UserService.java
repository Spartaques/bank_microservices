package com.andriibashuk.applicationservice.service;

import com.andriibashuk.applicationservice.security.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserService {
    public static Optional<User> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication.isAuthenticated()) {
            Optional.of((UserDetails) authentication.getPrincipal());
        }
        return Optional.empty();
    }

    public static boolean authenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication.isAuthenticated();
    }
}

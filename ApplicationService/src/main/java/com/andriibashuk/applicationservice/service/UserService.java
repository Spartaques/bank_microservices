package com.andriibashuk.applicationservice.service;

import com.andriibashuk.applicationservice.security.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.util.Optional;

public interface UserService {
    public static User getUser() {
        return (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static boolean authenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.isAuthenticated();
    }
}

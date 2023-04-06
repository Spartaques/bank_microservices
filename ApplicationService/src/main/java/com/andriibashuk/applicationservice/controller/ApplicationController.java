package com.andriibashuk.applicationservice.controller;

import com.andriibashuk.applicationservice.security.User;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationController {
    @GetMapping("/hi")
    public String hi(@AuthenticationPrincipal User principal) {
        System.out.println(principal);
        return "hi";
    }
}

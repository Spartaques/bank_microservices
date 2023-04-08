package com.andriibashuk.applicationservice.controller;

import com.andriibashuk.applicationservice.request.NewApplicationRequest;
import com.andriibashuk.applicationservice.response.ApplicationResponse;
import com.andriibashuk.applicationservice.security.User;
import com.andriibashuk.applicationservice.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.HashMap;

@RestController
public class ApplicationController {
    ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping("/hi")
    public String hi() {
        return "hi";
    }

    @PostMapping("/new")
    public ResponseEntity<ApplicationResponse> newApplication(@Valid @RequestBody NewApplicationRequest newApplicationRequest) {
        return new ResponseEntity<>(applicationService.newApplication(newApplicationRequest.getClientId(), newApplicationRequest.getRequestedAmount()), HttpStatus.CREATED);
    }
}



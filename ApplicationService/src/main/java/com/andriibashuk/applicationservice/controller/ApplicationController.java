package com.andriibashuk.applicationservice.controller;

import com.andriibashuk.applicationservice.security.User;
import lombok.extern.java.Log;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Log
@RestController
public class ApplicationController {
    @Autowired
    RestTemplate restTemplate;
    @GetMapping("/hi")
    public String hi(@AuthenticationPrincipal User principal) {
        HttpEntity<String> request = new HttpEntity<String>("{}");
        restTemplate.postForObject("http://ClientAuthService/register", request, SomeObject.class);
        log.info(principal.toString());
        return "hi";
    }
}



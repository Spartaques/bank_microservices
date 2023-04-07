package com.andriibashuk.applicationservice.controller;

import com.andriibashuk.applicationservice.security.User;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
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
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.HashMap;

@RestController
public class ApplicationController {
    Logger logger = LogManager.getLogger(ApplicationController.class);
    @Autowired
    RestTemplate restTemplate;
    @GetMapping("/hi")
    public String hi(@AuthenticationPrincipal User principal) {
        HashMap<String, String> map= new HashMap<>();
        map.put("test", "test");
        map.put("test1", "test");
        map.put("test2", "test");
        map.put("test3", "test");
        logger.info("HI MEN!", map);
        return "hi";
    }
}



package com.andriibashuk.applicationservice.service;

import com.andriibashuk.applicationservice.response.ApplicationResponse;
import org.springframework.http.ResponseEntity;

public interface ApplicationService {
    ApplicationResponse newApplication(Long clientId, Integer requestedAmount);
}

package com.andriibashuk.applicationservice.service;

import com.andriibashuk.applicationservice.entity.Application;
import com.andriibashuk.applicationservice.response.ApplicationResponse;
import com.andriibashuk.applicationservice.security.Client;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface ApplicationService {
    ApplicationResponse newApplication(Client client, Long clientId, Integer requestedAmount);

    ApplicationResponse approve(Long applicationId, Long userId, Integer approvedAmount);

    ApplicationResponse deny(Long id);

    ApplicationResponse sign(Long id, Client client);
}

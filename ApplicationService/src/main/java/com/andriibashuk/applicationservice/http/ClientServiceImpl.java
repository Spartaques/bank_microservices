package com.andriibashuk.applicationservice.http;

import com.andriibashuk.applicationservice.exception.ClientHttpServiceException;
import com.andriibashuk.applicationservice.security.Client;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@Log
public class ClientServiceImpl implements ClientService {
    private final RestTemplate restTemplate;

    public ClientServiceImpl(@Qualifier("client") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Override
    public Client getClientById(Long clientId) {
        try {
            return restTemplate.getForObject("/show/" + clientId, Client.class);
        } catch (RestClientException exception) {
            log.warning("Client service exception: " + exception.getMessage());
            throw new ClientHttpServiceException("Smth went wrong", HttpStatus.BAD_GATEWAY, "CLIENT_SERVICE_NOT_AVAILABLE");
        }
    }
}

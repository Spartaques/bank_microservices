package com.andriibashuk.applicationservice.http;

import com.andriibashuk.applicationservice.security.Client;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ClientServiceImpl implements ClientService{
    private final RestTemplate restTemplate;

    public ClientServiceImpl(@Qualifier("client") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Override
    public Client getClientById(Long clientId) {
        return restTemplate.getForObject("/show/"+clientId, Client.class);
    }
}

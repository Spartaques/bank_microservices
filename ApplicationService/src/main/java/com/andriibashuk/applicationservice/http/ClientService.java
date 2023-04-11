package com.andriibashuk.applicationservice.http;

import com.andriibashuk.applicationservice.security.Client;

public interface ClientService {
    public Client getClientById(Long clientId);
}

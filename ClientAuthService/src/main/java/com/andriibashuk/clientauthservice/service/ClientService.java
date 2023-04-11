package com.andriibashuk.clientauthservice.service;

import com.andriibashuk.clientauthservice.entity.Client;
import com.andriibashuk.clientauthservice.entity.Client.Gender;
import com.andriibashuk.clientauthservice.response.ClientResponse;

public interface ClientService {
    ClientResponse register(String firstName, String lastName, String email,String phone, String password, Short age, Gender gender);

    String login(String email, String password);

    ClientResponse getById(Long id);
}

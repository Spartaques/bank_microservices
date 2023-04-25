package com.andriibashuk.applicationservice;

import com.andriibashuk.applicationservice.entity.Application;
import com.andriibashuk.applicationservice.security.Client;
import com.github.javafaker.Faker;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

public class TestUtil {
    public static Client client()
    {
        Faker faker = new Faker();

        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(faker.date().future(1, TimeUnit.DAYS).toInstant(), ZoneId.systemDefault());

        return Client.builder()
                .id(1L)
                .email(faker.internet().emailAddress())
                .age((short) 30)
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .phone(faker.phoneNumber().phoneNumber())
                .createdDate(zonedDateTime)
                .lastModifiedDate(zonedDateTime)
                .createdBy(1L)
                .lastModifiedBy(2L)
                .build();
    }

    public static Application application(long clientId)
    {
        return Application.builder()
                .id(1L)
                .clientId(clientId)
                .requestedAmount(500)
                .status(Application.Status.NEW)
                .build();
    }
}

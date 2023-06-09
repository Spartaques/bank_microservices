package com.andriibashuk.applicationservice.kafka;

import com.andriibashuk.applicationservice.entity.Application;
import com.andriibashuk.applicationservice.security.Client;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationDto {
    private Long id;
    private Integer requestedAmount;
    private Integer approvedAmount;
    private Long clientId;
    private Long userId;
    private Application.Status status;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    Client client;
    private String sourceOfMessage;
}

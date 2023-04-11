package com.andriibashuk.tmservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.ZonedDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Application {
    private Long id;
    private Integer requestedAmount;
    private Integer approvedAmount;
    private Long clientId;
    private Long userId;
    private Status status;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    private Client client;

    public static enum Status {
        NEW,
        APPROVED,
        DENIED,
        SIGNED
    }
}

package com.andriibashuk.applicationservice.response;

import com.andriibashuk.applicationservice.entity.Application;
import com.andriibashuk.applicationservice.security.Client;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationResponse {
    private Long id;
    @NotNull private Integer requestedAmount;
    private Integer approvedAmount;
    private Long clientId;
    private Long userId;
    private Application.Status status;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}

package com.andriibashuk.applicationservice.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApproveApplicationRequest {
    @Min(value = 0)
    @Max(value = 100000)
    @NotNull
    private Integer approvedAmount;
}

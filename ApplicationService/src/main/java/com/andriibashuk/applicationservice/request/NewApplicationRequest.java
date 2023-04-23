package com.andriibashuk.applicationservice.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewApplicationRequest {
    @Min(value = 0)
    @Max(value = 100000)
    @NotNull
    private Integer requestedAmount;

}

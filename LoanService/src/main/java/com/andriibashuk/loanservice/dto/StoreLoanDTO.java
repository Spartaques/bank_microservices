package com.andriibashuk.loanservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StoreLoanDTO {
    @Min(10)
    @Max(1000)
    @NotNull
    int amount;
}

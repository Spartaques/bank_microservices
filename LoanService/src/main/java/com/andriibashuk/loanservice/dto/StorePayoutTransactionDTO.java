package com.andriibashuk.loanservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StorePayoutTransactionDTO {
    @Max(100000)
    @NotNull
    int amount;
}

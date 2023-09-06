package com.andriibashuk.loanservice.dto;

import com.andriibashuk.loanservice.entity.Transaction;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StoreTransactionDTO {
    @Max(100000)
    @NotNull
    int amount;

    @NotNull
    Transaction.Type type;
}

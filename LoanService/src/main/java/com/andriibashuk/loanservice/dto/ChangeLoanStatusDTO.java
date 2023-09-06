package com.andriibashuk.loanservice.dto;

import com.andriibashuk.loanservice.entity.Loan;
import lombok.Data;

@Data
public class ChangeLoanStatusDTO {
    Loan.Event event;
}

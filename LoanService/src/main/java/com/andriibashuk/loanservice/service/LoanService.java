package com.andriibashuk.loanservice.service;

public interface LoanService {
    void create(Long clientId, Long applicationId, Integer approvedAmount);
}

package com.andriibashuk.loanservice.controller;

import com.andriibashuk.loanservice.dto.StoreTransactionDTO;
import com.andriibashuk.loanservice.entity.Transaction;
import com.andriibashuk.loanservice.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(path = "transactions",name = "transactions")
@RestController
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("{loanId}")
    public Transaction store(@PathVariable Long loanId, @Valid @RequestBody StoreTransactionDTO storeTransactionDTO) {
        return this.transactionService.store(loanId, storeTransactionDTO.getAmount(), storeTransactionDTO.getType());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @GetMapping("{loanId}")
    public List<Transaction> byLoan(@PathVariable Long loanId) {
        return this.transactionService.allByLoan(loanId);
    }
}

package com.andriibashuk.loanservice.service;


import com.andriibashuk.loanservice.entity.Balance;
import com.andriibashuk.loanservice.entity.Loan;
import com.andriibashuk.loanservice.entity.Transaction;
import com.andriibashuk.loanservice.repository.BalanceRepository;
import com.andriibashuk.loanservice.repository.LoanRepository;
import com.andriibashuk.loanservice.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TransactionServiceImpl {
    private final TransactionRepository transactionRepository;
    private final LoanRepository loanRepository;
    private final BalanceRepository balanceRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, LoanRepository loanRepository, BalanceRepository balanceRepository) {
        this.transactionRepository = transactionRepository;
        this.loanRepository = loanRepository;
        this.balanceRepository = balanceRepository;
    }

    public Transaction store(Long loanId, int amount, Transaction.Type type) {
        Loan loan = this.loanRepository.findById(loanId).orElseThrow();
        Transaction transaction = new Transaction();
        transaction.setType(type);
        transaction.setAmount(amount, type);
        transaction.setLoan(loan);
        transaction = transactionRepository.save(transaction);
        recalculateBalance(transaction);
        return transaction;
    }

    public List<Transaction> allByLoan(Long loanId) {
        Optional<Loan> loan = this.loanRepository.findById(loanId);
        if(loan.isEmpty()) {
            throw new EntityNotFoundException();
        }

        return this.transactionRepository.getTransactionsByLoanIs(loan.get());
    }

    private void recalculateBalance(Transaction transaction) {
        Collection<Transaction> transactions = transactionRepository.getTransactionsByLoanIs(transaction.getLoan());
        int balanceSummary = 0 ;
        Map<String, Integer> resultMap = new HashMap<>();
        for (Transaction.Type type: Transaction.Type.values()) {
            if(!resultMap.containsKey(type.toString())) {
                resultMap.put(type.toString(), 0);
            }
        }
        for (Transaction item : transactions) {
            balanceSummary += item.getAmount();
            int prevValue = resultMap.get(item.getType().toString());
            resultMap.put(item.getType().toString(), prevValue+item.getAmount());
        }
        resultMap.put(Balance.BALANCE, balanceSummary);
        Balance balance = new Balance();
        balance.setBalanceData(resultMap);
        balanceRepository.save(balance);
        Loan loan = transaction.getLoan();
        loan.setBalance(balance);
        loanRepository.save(loan);
    }
}

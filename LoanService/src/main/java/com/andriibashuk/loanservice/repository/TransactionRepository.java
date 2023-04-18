package com.andriibashuk.loanservice.repository;

import com.andriibashuk.loanservice.entity.Loan;
import com.andriibashuk.loanservice.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    public List<Transaction> getTransactionsByLoanIs(Loan loan);
}

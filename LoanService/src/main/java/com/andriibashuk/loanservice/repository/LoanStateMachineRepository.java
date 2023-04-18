package com.andriibashuk.loanservice.repository;

import com.andriibashuk.loanservice.entity.Loan;
import org.springframework.statemachine.data.StateMachineRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanStateMachineRepository extends StateMachineRepository<Loan> {
}

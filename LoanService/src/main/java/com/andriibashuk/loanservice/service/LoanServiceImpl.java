package com.andriibashuk.loanservice.service;

import com.andriibashuk.loanservice.entity.Loan;
import com.andriibashuk.loanservice.repository.LoanRepository;
import com.andriibashuk.loanservice.statemachine.LoanPersistingStateMachineInterceptor;
import lombok.extern.java.Log;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;

@Service
@Log
public class LoanServiceImpl implements LoanService {
    LoanRepository loanRepository;

    private final StateMachineFactory<Loan.Status, Loan.Event> factory;

    private final StateMachinePersister<Loan.Status, Loan.Event, String> persister;

    public LoanServiceImpl(LoanRepository loanRepository, StateMachineFactory<Loan.Status, Loan.Event> factory, LoanPersistingStateMachineInterceptor<Loan.Status, Loan.Event, String> loanPersistingStateMachineInterceptor) {
        this.loanRepository = loanRepository;
        this.factory = factory;
        this.persister = new DefaultStateMachinePersister<>(loanPersistingStateMachineInterceptor);
    }
    @Override
    public void create(Long clientId, Long applicationId, Integer approvedAmount) {
        Loan loan = new Loan();
        loan.setAmount(approvedAmount);
        loan.setClientId(clientId);
        loan.setApplicationId(applicationId);
        loan.setStatus(Loan.Status.NEW);
        loanRepository.save(loan);
        StateMachine<Loan.Status, Loan.Event> stateMachine = factory.getStateMachine(loan.getMachineId());
        stateMachine.start();
        try {
            persister.persist(stateMachine, loan.getMachineId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        log.info("loan created: "+loan);
    }
}

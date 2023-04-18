package com.andriibashuk.loanservice.statemachine;

import com.andriibashuk.loanservice.entity.Loan;
import lombok.extern.java.Log;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Service;

@Service
@Log
public class BaseAction implements Action<Loan.Status, Loan.Event> {

    @Override
    public void execute(StateContext<Loan.Status, Loan.Event> stateContext) {

    }


}

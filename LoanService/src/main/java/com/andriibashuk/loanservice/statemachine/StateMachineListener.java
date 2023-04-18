package com.andriibashuk.loanservice.statemachine;

import lombok.extern.java.Log;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Service;

@Log
@Service
public class StateMachineListener<LoanStatus, LoanEvent> implements org.springframework.statemachine.listener.StateMachineListener<LoanStatus, LoanEvent> {

    @Override
    public void stateChanged(State<LoanStatus, LoanEvent> from, State<LoanStatus, LoanEvent> to) {

    }

    @Override
    public void stateEntered(State<LoanStatus, LoanEvent> state) {

    }

    @Override
    public void stateExited(State<LoanStatus, LoanEvent> state) {

    }

    @Override
    public void eventNotAccepted(Message<LoanEvent> message){
        log.info(String.format("eventNotAccepted(%s)", message.getPayload()));
    }

    @Override
    public void transition(Transition<LoanStatus, LoanEvent> transition) {

    }

    @Override
    public void transitionStarted(Transition<LoanStatus, LoanEvent> transition) {

    }

    @Override
    public void transitionEnded(Transition<LoanStatus, LoanEvent> transition) {

    }

    @Override
    public void stateMachineStarted(StateMachine<LoanStatus, LoanEvent> stateMachine) {

    }

    @Override
    public void stateMachineStopped(StateMachine<LoanStatus, LoanEvent> stateMachine) {
        log.info(String.format("stateMachineStopped(%s)", stateMachine.getId()));
    }

    @Override
    public void stateMachineError(StateMachine<LoanStatus, LoanEvent> stateMachine, Exception exception) {
        log.info(String.format("stateMachineError(id: %s, error: %s)", stateMachine.getId(), exception.getMessage()));
    }

    @Override
    public void extendedStateChanged(Object key, Object value) {

    }

    @Override
    public void stateContext(StateContext<LoanStatus, LoanEvent> stateContext) {

    }
}

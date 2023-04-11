package com.andriibashuk.applicationservice.statemachine;

import lombok.extern.java.Log;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Service;

@Log
@Service
public class StateMachineListener<S, E> implements org.springframework.statemachine.listener.StateMachineListener<S, E> {

    @Override
    public void stateChanged(State<S, E> from, State<S, E> to) {

    }

    @Override
    public void stateEntered(State<S, E> state) {

    }

    @Override
    public void stateExited(State<S, E> state) {

    }

    @Override
    public void eventNotAccepted(Message<E> message){
        log.info(String.format("eventNotAccepted(%s)", message.getPayload()));
    }

    @Override
    public void transition(Transition<S, E> transition) {

    }

    @Override
    public void transitionStarted(Transition<S, E> transition) {

    }

    @Override
    public void transitionEnded(Transition<S, E> transition) {

    }

    @Override
    public void stateMachineStarted(StateMachine<S, E> stateMachine) {

    }

    @Override
    public void stateMachineStopped(StateMachine<S, E> stateMachine) {
        log.info(String.format("stateMachineStopped(%s)", stateMachine.getId()));
    }

    @Override
    public void stateMachineError(StateMachine<S, E> stateMachine, Exception exception) {
        log.info(String.format("stateMachineError(id: %s, error: %s)", stateMachine.getId(), exception.getMessage()));
    }

    @Override
    public void extendedStateChanged(Object key, Object value) {

    }

    @Override
    public void stateContext(StateContext<S, E> stateContext) {

    }
}

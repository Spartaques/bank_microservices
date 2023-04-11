package com.andriibashuk.tmservice.statemachine.actions;

import com.andriibashuk.tmservice.entity.Ticket;
import lombok.extern.java.Log;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Component
@Log
public class Failed implements Action<Ticket.Status, Ticket.Event> {
    @Override
    public void execute(StateContext<Ticket.Status, Ticket.Event> stateContext) {

    }
}

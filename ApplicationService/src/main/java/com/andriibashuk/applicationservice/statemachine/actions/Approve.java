package com.andriibashuk.applicationservice.statemachine.actions;

import com.andriibashuk.applicationservice.entity.Application;
import lombok.extern.java.Log;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Service;

@Service
@Log
public class Approve implements Action<Application.Status, Application.Event> {
    @Override
    public void execute(StateContext<Application.Status, Application.Event> stateContext) {
        log.info("Approve");
    }
}

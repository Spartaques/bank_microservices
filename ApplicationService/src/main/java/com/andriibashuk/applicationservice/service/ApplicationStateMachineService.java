package com.andriibashuk.applicationservice.service;

import com.andriibashuk.applicationservice.entity.Application;
import com.andriibashuk.applicationservice.security.Client;
import org.springframework.statemachine.StateMachine;

public interface ApplicationStateMachineService {

    public void initialStatus(Application application);
    public void sendEvent(Application application, Client client, Application.Event event);
}

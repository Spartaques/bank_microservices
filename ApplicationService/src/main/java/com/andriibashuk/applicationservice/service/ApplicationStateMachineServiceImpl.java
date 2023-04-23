package com.andriibashuk.applicationservice.service;

import com.andriibashuk.applicationservice.entity.Application;
import com.andriibashuk.applicationservice.exception.StateMachineEventNotAcceptedException;
import com.andriibashuk.applicationservice.security.Client;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ApplicationStateMachineServiceImpl implements ApplicationStateMachineService {
    private final StateMachineFactory<Application.Status, Application.Event> factory;

    private final StateMachinePersister<Application.Status, Application.Event, String> persister;

    public ApplicationStateMachineServiceImpl(StateMachineFactory<Application.Status, Application.Event> factory, StateMachinePersister<Application.Status, Application.Event, String> persister) {
        this.factory = factory;
        this.persister = persister;
    }

    @Override
    public void initialStatus(Application application) {
        StateMachine<Application.Status, Application.Event> stateMachine = factory.getStateMachine(application.getMachineId());
        stateMachine.start();
        try {
            persister.persist(stateMachine, application.getMachineId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendEvent(Application application, Client client, Application.Event event) {
        StateMachine<Application.Status, Application.Event> stateMachine = this.factory.getStateMachine(application.getMachineId());
        try {
            persister.restore(stateMachine, application.getMachineId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Map<Object, Object> variables = stateMachine.getExtendedState().getVariables();
        variables.put("application", application);
        variables.put("client", client);
        boolean accepted = stateMachine.sendEvent(MessageBuilder.withPayload(event).build());
        if (!accepted) {
            throw new StateMachineEventNotAcceptedException("This state is not acceptable", HttpStatus.BAD_REQUEST, "STATE_NOT_ACCEPTED");
        }
        try {
            persister.persist(stateMachine, application.getMachineId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        stateMachine.stop();
    }
}

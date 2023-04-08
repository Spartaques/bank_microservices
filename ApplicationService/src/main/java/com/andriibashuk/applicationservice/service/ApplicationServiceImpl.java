package com.andriibashuk.applicationservice.service;

import com.andriibashuk.applicationservice.entity.Application;
import com.andriibashuk.applicationservice.repository.ApplicationRepository;
import com.andriibashuk.applicationservice.response.ApplicationResponse;
import com.andriibashuk.applicationservice.statemachine.ApplicationPersistingStateMachineInterceptor;
import org.springframework.http.ResponseEntity;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ApplicationServiceImpl implements ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final StateMachineFactory<Application.Status, Application.Event> factory;

    private final StateMachinePersister<Application.Status, Application.Event, String> persister;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository, StateMachineFactory<Application.Status, Application.Event> factory, ApplicationPersistingStateMachineInterceptor<Application.Status, Application.Event, String> applicationPersistingStateMachineInterceptor) {
        this.applicationRepository = applicationRepository;
        this.factory = factory;
        this.persister = new DefaultStateMachinePersister<>(applicationPersistingStateMachineInterceptor);
    }

    @Transactional
    @Override
    public ApplicationResponse newApplication(Long clientId, Integer requestedAmount) {
        Application application = new Application();
        application.setRequestedAmount(requestedAmount);
        application.setClientId(clientId);
        application.setStatus(Application.Status.NEW);
        applicationRepository.save(application);
        StateMachine<Application.Status, Application.Event> stateMachine = factory.getStateMachine(application.getMachineId());
        stateMachine.start();
        try {
            persister.persist(stateMachine, application.getMachineId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return ApplicationResponse.builder()
                .id(application.getId())
                .requestedAmount(application.getRequestedAmount())
                .approvedAmount(application.getApprovedAmount())
                .clientId(application.getClientId())
                .userId(application.getUserId())
                .status(application.getStatus())
                .createdAt(application.getCreatedAt())
                .updatedAt(application.getUpdatedAt())
                .build();
    }
}

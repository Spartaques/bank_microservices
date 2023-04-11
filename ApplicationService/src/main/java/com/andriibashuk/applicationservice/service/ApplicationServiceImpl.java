package com.andriibashuk.applicationservice.service;

import com.andriibashuk.applicationservice.entity.Application;
import com.andriibashuk.applicationservice.exception.ApplicationNotFoundException;
import com.andriibashuk.applicationservice.exception.ClientHttpServiceException;
import com.andriibashuk.applicationservice.exception.StateMachineEventNotAcceptedException;
import com.andriibashuk.applicationservice.http.ClientService;
import com.andriibashuk.applicationservice.repository.ApplicationRepository;
import com.andriibashuk.applicationservice.response.ApplicationResponse;
import com.andriibashuk.applicationservice.security.Client;
import com.andriibashuk.applicationservice.statemachine.ApplicationPersistingStateMachineInterceptor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;

import java.util.Optional;

@Service
@Log
public class ApplicationServiceImpl implements ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final StateMachineFactory<Application.Status, Application.Event> factory;

    private final StateMachinePersister<Application.Status, Application.Event, String> persister;
    private final ClientService clientService;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository, StateMachineFactory<Application.Status, Application.Event> factory, ApplicationPersistingStateMachineInterceptor<Application.Status, Application.Event, String> applicationPersistingStateMachineInterceptor, ClientService clientService) {
        this.applicationRepository = applicationRepository;
        this.factory = factory;
        this.persister = new DefaultStateMachinePersister<>(applicationPersistingStateMachineInterceptor);
        this.clientService = clientService;
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

    @Transactional
    @Override
    public ApplicationResponse approve(Long id, Long userId, Integer approvedAmount) {
        Optional<Application> optionalApplication = this.applicationRepository.findById(id);
        if (optionalApplication.isEmpty()) {
            throw new ApplicationNotFoundException("Application not found", HttpStatus.NOT_FOUND, "APPLICATION_NOT_FOUND");
        }
        Application application = optionalApplication.get();
        Client client;
        try {
            client = clientService.getClientById(application.getClientId());
        } catch (RestClientException exception) {
            log.warning("Client service exception: "+ exception.getMessage());
            throw new ClientHttpServiceException("Smth went wrong", HttpStatus.BAD_GATEWAY, "CLIENT_SERVICE_NOT_AVAILABLE");
        }
        StateMachine<Application.Status, Application.Event> stateMachine = this.factory.getStateMachine(id.toString());
        try {
            persister.restore(stateMachine, id.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("applicationID "+application.getId());
        application.setApprovedAmount(approvedAmount);
        application.setUserId(userId);
        applicationRepository.save(application);

        stateMachine.getExtendedState().getVariables().put("application", application);
        stateMachine.getExtendedState().getVariables().put("client", client);
        boolean accepted = stateMachine.sendEvent(MessageBuilder.withPayload(Application.Event.APPROVE).build());
        if (!accepted) {
            throw new StateMachineEventNotAcceptedException("This state is not acceptable", HttpStatus.BAD_REQUEST, "STATE_NOT_ACCEPTED");
        }
        try {
            persister.persist(stateMachine, id.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        stateMachine.stop();
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

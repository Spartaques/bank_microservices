package com.andriibashuk.applicationservice.service;

import com.andriibashuk.applicationservice.entity.Application;
import com.andriibashuk.applicationservice.exception.ApplicationNotFoundException;
import com.andriibashuk.applicationservice.exception.ClientHttpServiceException;
import com.andriibashuk.applicationservice.exception.StateMachineEventNotAcceptedException;
import com.andriibashuk.applicationservice.http.ClientService;
import com.andriibashuk.applicationservice.kafka.ApplicationDto;
import com.andriibashuk.applicationservice.repository.ApplicationRepository;
import com.andriibashuk.applicationservice.response.ApplicationResponse;
import com.andriibashuk.applicationservice.security.Client;
import com.andriibashuk.applicationservice.statemachine.ApplicationPersistingStateMachineInterceptor;
import lombok.extern.java.Log;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;

import java.util.Optional;

import static com.andriibashuk.applicationservice.service.UserService.getClient;

@Service
@Log
public class ApplicationServiceImpl implements ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final StateMachineFactory<Application.Status, Application.Event> factory;

    private final StateMachinePersister<Application.Status, Application.Event, String> persister;
    private final ClientService clientService;

    KafkaTemplate<Integer, Object> template;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository, StateMachineFactory<Application.Status, Application.Event> factory, ApplicationPersistingStateMachineInterceptor<Application.Status, Application.Event, String> applicationPersistingStateMachineInterceptor, ClientService clientService, KafkaTemplate<Integer, Object> kafkaTemplate) {
        this.applicationRepository = applicationRepository;
        this.factory = factory;
        this.persister = new DefaultStateMachinePersister<>(applicationPersistingStateMachineInterceptor);
        this.clientService = clientService;
        this.template = kafkaTemplate;
    }

    @Transactional(transactionManager = "chainedKafkaTransactionManager")
    @Override
    public ApplicationResponse newApplication(Client client, Long clientId, Integer requestedAmount) {
        try {
            clientService.getClientById(clientId);
        } catch (RestClientException exception) {
            log.warning("Client service exception: "+ exception.getMessage());
            throw new ClientHttpServiceException("Smth went wrong", HttpStatus.BAD_GATEWAY, "CLIENT_SERVICE_NOT_AVAILABLE");
        }
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

        ApplicationDto applicationDto = ApplicationDto.builder()
                .id(application.getId())
                .requestedAmount(application.getRequestedAmount())
                .approvedAmount(application.getApprovedAmount())
                .clientId(application.getClientId())
                .client(client)
                .userId(application.getUserId())
                .status(Application.Status.NEW)
                .createdAt(application.getCreatedAt())
                .updatedAt(application.getUpdatedAt())
                .sourceOfMessage("created")
                .build();

        log.info("in transaction: "+this.template.inTransaction());

        this.template.send("application", Math.toIntExact(application.getId()), applicationDto);

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

    @Transactional(transactionManager = "chainedKafkaTransactionManager")
    @Override
    public ApplicationResponse approve(Long id, Long userId, Integer approvedAmount) {
        Application application = getApplication(id);
        Client client;
        try {
            client = clientService.getClientById(application.getClientId());
        } catch (RestClientException exception) {
            log.warning("Client service exception: "+ exception.getMessage());
            throw new ClientHttpServiceException("Smth went wrong", HttpStatus.BAD_GATEWAY, "CLIENT_SERVICE_NOT_AVAILABLE");
        }
        log.info("applicationID "+application.getId());
        application.setApprovedAmount(approvedAmount);
        application.setUserId(userId);
        applicationRepository.save(application);

        sendEventToStateMachine(id.toString(), Application.Event.APPROVE, application, client);

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

    @Transactional(transactionManager = "chainedKafkaTransactionManager")
    @Override
    public ApplicationResponse deny(Long id) {
        Application application = getApplication(id);
        Client client;
        try {
            client = clientService.getClientById(application.getClientId());
        } catch (RestClientException exception) {
            log.warning("Client service exception: "+ exception.getMessage());
            throw new ClientHttpServiceException("Smth went wrong", HttpStatus.BAD_GATEWAY, "CLIENT_SERVICE_NOT_AVAILABLE");
        }

        sendEventToStateMachine(id.toString(), Application.Event.DENY, application, client);

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

    @Override
    public ApplicationResponse sign(Long id, Client client) {
        Application application = getApplication(id);

        sendEventToStateMachine(id.toString(), Application.Event.SIGN, application, client);

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

    private Application getApplication(Long id) {
        Optional<Application> optionalApplication = this.applicationRepository.findById(id);
        if (optionalApplication.isEmpty()) {
            throw new ApplicationNotFoundException("Application not found", HttpStatus.NOT_FOUND, "APPLICATION_NOT_FOUND");
        }
        return optionalApplication.get();
    }

    private void sendEventToStateMachine(String id, Application.Event event, Application application, Client client) {
        StateMachine<Application.Status, Application.Event> stateMachine = this.factory.getStateMachine(id.toString());
        try {
            persister.restore(stateMachine, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        stateMachine.getExtendedState().getVariables().put("application", application);
        stateMachine.getExtendedState().getVariables().put("client", client);
        boolean accepted = stateMachine.sendEvent(MessageBuilder.withPayload(event).build());
        if (!accepted) {
            throw new StateMachineEventNotAcceptedException("This state is not acceptable", HttpStatus.BAD_REQUEST, "STATE_NOT_ACCEPTED");
        }
        try {
            persister.persist(stateMachine, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        stateMachine.stop();
    }
}

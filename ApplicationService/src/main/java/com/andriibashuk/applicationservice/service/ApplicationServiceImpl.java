package com.andriibashuk.applicationservice.service;

import com.andriibashuk.applicationservice.entity.Application;
import com.andriibashuk.applicationservice.exception.ApplicationNotFoundException;
import com.andriibashuk.applicationservice.exception.ClientHttpServiceException;
import com.andriibashuk.applicationservice.http.ClientService;
import com.andriibashuk.applicationservice.kafka.ApplicationDto;
import com.andriibashuk.applicationservice.repository.ApplicationRepository;
import com.andriibashuk.applicationservice.response.ApplicationResponse;
import com.andriibashuk.applicationservice.security.Client;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;

import java.util.Optional;

@Service
@Log
public class ApplicationServiceImpl implements ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final ClientService clientService;

    private final KafkaTemplate<Integer, Object> template;

    private final ModelMapper modelMapper;

    private final ApplicationStateMachineService applicationStateMachineService;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository, ClientService clientService, KafkaTemplate<Integer, Object> kafkaTemplate, ModelMapper modelMapper, ApplicationStateMachineService applicationStateMachineService) {
        this.applicationRepository = applicationRepository;
        this.clientService = clientService;
        this.template = kafkaTemplate;
        this.modelMapper = modelMapper;
        this.applicationStateMachineService = applicationStateMachineService;
    }

    @Transactional(transactionManager = "chainedKafkaTransactionManager")
    @Override
    public ApplicationResponse newApplication(Client client,  Integer requestedAmount) {
        try {
            clientService.getClientById(client.getId());
        } catch (RestClientException exception) {
            log.warning("Client service exception: "+ exception.getMessage());
            throw new ClientHttpServiceException("Something went wrong", HttpStatus.BAD_GATEWAY, "CLIENT_SERVICE_NOT_AVAILABLE");
        }
        Application application = new Application();
        application.setRequestedAmount(requestedAmount);
        application.setClientId(client.getId());
        application.setStatus(Application.Status.NEW);
        application = applicationRepository.save(application);
        applicationStateMachineService.initialStatus(application);
        ApplicationDto applicationDto = modelMapper.map(application, ApplicationDto.class);
        applicationDto.setSourceOfMessage("created");
        applicationDto.setStatus(Application.Status.NEW);

        template.send("application", Math.toIntExact(application.getId()), applicationDto);

        return modelMapper.map(application, ApplicationResponse.class);
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

        applicationStateMachineService.sendEvent(application, client, Application.Event.APPROVE);

        return modelMapper.map(application, ApplicationResponse.class);
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

        applicationStateMachineService.sendEvent(application, client, Application.Event.DENY);

        return modelMapper.map(application, ApplicationResponse.class);
    }

    @Transactional(transactionManager = "chainedKafkaTransactionManager")
    @Override
    public ApplicationResponse sign(Long id, Client client) {
        Application application = getApplication(id);

        applicationStateMachineService.sendEvent(application, client, Application.Event.SIGN);

        return modelMapper.map(application, ApplicationResponse.class);
    }

    private Application getApplication(Long id) {
        Optional<Application> optionalApplication = this.applicationRepository.findByIdLocked(id);
        if (optionalApplication.isEmpty()) {
            throw new ApplicationNotFoundException("Application not found", HttpStatus.NOT_FOUND, "APPLICATION_NOT_FOUND");
        }
        return optionalApplication.get();
    }
}

package com.andriibashuk.applicationservice.service;

import com.andriibashuk.applicationservice.TestUtil;
import com.andriibashuk.applicationservice.config.ModelMapperConfig;
import com.andriibashuk.applicationservice.entity.Application;
import com.andriibashuk.applicationservice.exception.ApplicationNotFoundException;
import com.andriibashuk.applicationservice.exception.ClientHttpServiceException;
import com.andriibashuk.applicationservice.http.ClientService;
import com.andriibashuk.applicationservice.kafka.ApplicationDto;
import com.andriibashuk.applicationservice.repository.ApplicationRepository;
import com.andriibashuk.applicationservice.response.ApplicationResponse;
import com.andriibashuk.applicationservice.security.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.client.RestClientException;

import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceImplTest {
    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private KafkaTemplate<Integer, Object> template;

    @Mock
    private ApplicationStateMachineService applicationStateMachineService;

    @Mock
    private ClientService clientService;

    private final ModelMapper modelMapper = new ModelMapperConfig().modelMapper();


    private ApplicationServiceImpl applicationService;

    private Client client;
    private Application application;

    @BeforeEach
    public void beforeEach() {
        client = TestUtil.client();

        application = TestUtil.application(client.getId());

        this.applicationService = new ApplicationServiceImpl(applicationRepository, clientService, template, modelMapper, applicationStateMachineService);
    }

    @Nested
    @DisplayName("newApplication")
    class NewApplication {

        @DisplayName("when correct input then newApplication")
        @Test
        void creates() throws Exception {
            given(clientService.getClientById(client.getId())).willReturn(client);
            given(applicationRepository.save(any(Application.class))).willReturn(application);
            doNothing().when(applicationStateMachineService).initialStatus(application);
            given(template.send(any(String.class), any(Integer.class), any(ApplicationDto.class))).willReturn(any());
            ApplicationResponse applicationResponse = applicationService.newApplication(client, application.getRequestedAmount());
            assertThat(applicationResponse).isEqualTo(modelMapper.map(application, ApplicationResponse.class));
            verify(clientService, times(1)).getClientById(client.getId());
        }
    }

    @Nested
    @DisplayName("approve")
    class Approve {
        @DisplayName("when correct input then approve")
        @Test
        void whenCorrectInputThenApprove()
        {
            given(clientService.getClientById(client.getId())).willReturn(client);
            given(applicationRepository.findByIdLocked(1L)).willReturn(Optional.of(application));
            given(applicationRepository.save(any(Application.class))).willReturn(application);
            doNothing().when(applicationStateMachineService).sendEvent(application, client, Application.Event.APPROVE);
            Application expectedApplication = Application.builder().id(1L).userId(1L).approvedAmount(300).requestedAmount(application.getRequestedAmount()).clientId(application.getClientId()).status(Application.Status.NEW).build();
            ApplicationResponse applicationResponse = applicationService.approve(1L, 1L, 300);
            verify(applicationStateMachineService, times(1)).sendEvent(application, client, Application.Event.APPROVE);
            verify(applicationRepository, times(1)).save(any(Application.class));
            verify(applicationRepository, times(1)).findByIdLocked(1L);
            assertThat(applicationResponse).isEqualTo(modelMapper.map(expectedApplication, ApplicationResponse.class));
        }
        @DisplayName("when not found application throws exception")
        @Test
        void whenNotFoundApplicationThenThrowsException()
        {
            given(applicationRepository.findByIdLocked(1L)).willReturn(Optional.empty());
            ApplicationNotFoundException e = assertThrows(ApplicationNotFoundException.class, () -> {
                applicationService.approve(1L, 1L, 300);
            });
            assertThat(e.getErrorCode()).isEqualTo("APPLICATION_NOT_FOUND");
            assertThat(e.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(e.getMessage()).isEqualTo("Application not found");
        }
    }

    @Nested
    @DisplayName("deny")
    class Deny {
        @Test
        @DisplayName("when correct input then deny")
        void whenCorrectInputThenDeny()
        {
            given(clientService.getClientById(client.getId())).willReturn(client);
            given(applicationRepository.findByIdLocked(1L)).willReturn(Optional.of(application));
            doNothing().when(applicationStateMachineService).sendEvent(application, client, Application.Event.DENY);
            ApplicationResponse applicationResponse = applicationService.deny(1L);
            verify(applicationStateMachineService, times(1)).sendEvent(application, client, Application.Event.DENY);
            verify(applicationRepository, times(1)).findByIdLocked(1L);
            assertThat(applicationResponse).isEqualTo(modelMapper.map(application, ApplicationResponse.class));
        }
    }

    @Nested
    @DisplayName("sign")
    class Sign
    {
        @Test
        @DisplayName("when correct input then sign")
        void whenCorrectInputThenSign()
        {
            given(applicationRepository.findByIdLocked(1L)).willReturn(Optional.of(application));
            doNothing().when(applicationStateMachineService).sendEvent(application, client, Application.Event.SIGN);
            ApplicationResponse applicationResponse = applicationService.sign(1L, client);
            verify(applicationStateMachineService, times(1)).sendEvent(application, client, Application.Event.SIGN);
            verify(applicationRepository, times(1)).findByIdLocked(1L);
            assertThat(applicationResponse).isEqualTo(modelMapper.map(application, ApplicationResponse.class));
        }
    }
}
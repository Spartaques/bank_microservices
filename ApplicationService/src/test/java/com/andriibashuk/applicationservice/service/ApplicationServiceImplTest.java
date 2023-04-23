package com.andriibashuk.applicationservice.service;

import com.andriibashuk.applicationservice.config.ModelMapperConfig;
import com.andriibashuk.applicationservice.entity.Application;
import com.andriibashuk.applicationservice.exception.ClientHttpServiceException;
import com.andriibashuk.applicationservice.http.ClientService;
import com.andriibashuk.applicationservice.kafka.ApplicationDto;
import com.andriibashuk.applicationservice.repository.ApplicationRepository;
import com.andriibashuk.applicationservice.response.ApplicationResponse;
import com.andriibashuk.applicationservice.security.Client;
import com.github.javafaker.Faker;
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

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

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


    private final Faker faker = new Faker();

    ApplicationServiceImpl applicationService;

    private Client client;
    Application application;

    @BeforeEach
    public void beforeEach() {
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(faker.date().future(1, TimeUnit.DAYS).toInstant(), ZoneId.systemDefault());
        client = Client.builder()
                .id(1L)
                .email(faker.internet().emailAddress())
                .age((short) 30)
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .phone(faker.phoneNumber().phoneNumber())
                .createdDate(zonedDateTime)
                .lastModifiedDate(zonedDateTime)
                .createdBy(1L)
                .lastModifiedBy(2L)
                .build();

        application = Application.builder()
                .id(1L)
                .clientId(client.getId())
                .requestedAmount(500)
                .status(Application.Status.NEW)
                .build();

        this.applicationService = new ApplicationServiceImpl(applicationRepository, clientService, template, modelMapper, applicationStateMachineService);
    }

    @Nested
    @DisplayName("new application")
    class NewApplication {

        @DisplayName("positive case")
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

        @DisplayName("throw exception when client not exists")
        @Test
        void assertThrowsExceptionWhenClientNotExists() throws Exception{
            given(clientService.getClientById(client.getId())).willThrow(new RestClientException(""));
            ClientHttpServiceException e = assertThrows(ClientHttpServiceException.class, () -> {
                applicationService.newApplication(client, application.getRequestedAmount());
            });
            assertThat(e.getErrorCode().equals("CLIENT_SERVICE_NOT_AVAILABLE"));
            assertThat(e.getHttpStatus().equals(HttpStatus.BAD_GATEWAY));
            assertThat(e.getMessage().equals("Something went wrong"));
        }
    }

    @Test
    void approve() {
    }

    @Test
    void deny() {
    }

    @Test
    void sign() {
    }


}
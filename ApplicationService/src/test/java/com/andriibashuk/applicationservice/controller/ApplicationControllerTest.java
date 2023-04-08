package com.andriibashuk.applicationservice.controller;

import com.andriibashuk.applicationservice.entity.Application;
import com.andriibashuk.applicationservice.request.NewApplicationRequest;
import com.andriibashuk.applicationservice.response.ApplicationResponse;
import com.andriibashuk.applicationservice.service.ApplicationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ApplicationControllerTest {
    @MockBean
    ApplicationService applicationService;

    @Test
    @DisplayName("test new application creates successfully")
    void newApplication() {
        ApplicationController applicationController = new ApplicationController(applicationService);
        ApplicationResponse applicationResponse = new ApplicationResponse(1L, 1, 1,1L, 1L, Application.Status.NEW, null, null);
        Mockito.when(applicationService.newApplication(1L,1)).thenReturn(applicationResponse);
        ResponseEntity<ApplicationResponse> applicationResponseResponseEntity = applicationController.newApplication(new NewApplicationRequest(1, 1L));
        assertThat(applicationResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(applicationResponseResponseEntity.getBody()).isEqualTo(applicationResponse);

    }
}
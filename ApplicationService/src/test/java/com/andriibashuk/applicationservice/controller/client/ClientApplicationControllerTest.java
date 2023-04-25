package com.andriibashuk.applicationservice.controller.client;

import com.andriibashuk.applicationservice.TestUtil;
import com.andriibashuk.applicationservice.request.NewApplicationRequest;
import com.andriibashuk.applicationservice.response.ApplicationResponse;
import com.andriibashuk.applicationservice.security.Client;
import com.andriibashuk.applicationservice.service.ApplicationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientApplicationControllerTest {
    @Mock
    private ApplicationService applicationService;

    @InjectMocks
    private ClientApplicationController clientApplicationController;

    private Client client;

    @Test
    void newApplication() {
        client = TestUtil.client();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(client,""));
        when(applicationService.newApplication(any(Client.class), anyInt())).thenReturn(any(ApplicationResponse.class));
        ResponseEntity<ApplicationResponse> applicationResponseResponseEntity = clientApplicationController.newApplication(new NewApplicationRequest(anyInt()));
        verify(applicationService, times(1)).newApplication(any(Client.class), anyInt());
        assertThat(applicationResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void sign() {

    }
}
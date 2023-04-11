package com.andriibashuk.applicationservice.statemachine.actions;

import com.andriibashuk.applicationservice.entity.Application;
import com.andriibashuk.applicationservice.kafka.ApplicationDTO;
import com.andriibashuk.applicationservice.response.ApplicationResponse;
import com.andriibashuk.applicationservice.security.Client;
import com.andriibashuk.applicationservice.service.ApplicationService;
import com.andriibashuk.applicationservice.service.ApplicationStatusChangelogService;
import com.andriibashuk.applicationservice.service.UserService;
import lombok.extern.java.Log;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Service;

@Service
@Log
public class Approve implements Action<Application.Status, Application.Event> {
    ApplicationStatusChangelogService applicationStatusChangelogService;

    KafkaTemplate<String, ApplicationDTO> template;

    public Approve(ApplicationStatusChangelogService applicationStatusChangelogService, KafkaTemplate template) {
        this.applicationStatusChangelogService = applicationStatusChangelogService;
        this.template = template;
    }

    @Override
    public void execute(StateContext<Application.Status, Application.Event> stateContext) {
        Application application = (Application) stateContext.getExtendedState().getVariables().get("application");
        Client client = (Client) stateContext.getExtendedState().getVariables().get("client");
        applicationStatusChangelogService.create(application, stateContext.getSource().getId(), stateContext.getTarget().getId(), UserService.getUser().getId());
        ApplicationDTO applicationDTO = ApplicationDTO.builder()
                .id(application.getId())
                .requestedAmount(application.getRequestedAmount())
                .approvedAmount(application.getApprovedAmount())
                .client(client)
                .userId(application.getUserId())
                .status(stateContext.getTarget().getId())
                .createdAt(application.getCreatedAt())
                .updatedAt(application.getUpdatedAt())
                .build();
        template.send("application", applicationDTO );
    }
}

package com.andriibashuk.applicationservice.statemachine.actions;

import com.andriibashuk.applicationservice.entity.Application;
import com.andriibashuk.applicationservice.kafka.ApplicationDto;
import com.andriibashuk.applicationservice.security.Client;
import com.andriibashuk.applicationservice.service.ApplicationStatusChangelogService;
import com.andriibashuk.applicationservice.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Log
public class BaseAction implements Action<Application.Status, Application.Event> {
    ApplicationStatusChangelogService applicationStatusChangelogService;

    KafkaTemplate<Integer, Object> template;

    public BaseAction(@NotNull ApplicationStatusChangelogService applicationStatusChangelogService, @NotNull KafkaTemplate<Integer, Object> template) {
        this.applicationStatusChangelogService = applicationStatusChangelogService;
        this.template = template;
    }

    @Override
    public void execute(StateContext<Application.Status, Application.Event> stateContext) {
        try {
            Application application = (Application) stateContext.getExtendedState().getVariables().get("application");
            Client client = (Client) stateContext.getExtendedState().getVariables().get("client");
            applicationStatusChangelogService.create(application, stateContext.getSource().getId(), stateContext.getTarget().getId(), application.getUserId());
            log.info("application status changelog created");
            ApplicationDto applicationDto = ApplicationDto.builder()
                    .id(application.getId())
                    .requestedAmount(application.getRequestedAmount())
                    .approvedAmount(application.getApprovedAmount())
                    .clientId(application.getClientId())
                    .client(client)
                    .userId(application.getUserId())
                    .status(stateContext.getTarget().getId())
                    .createdAt(application.getCreatedAt())
                    .updatedAt(application.getUpdatedAt())
                    .sourceOfMessage("changeStatus")
                    .build();
            template.send("application", applicationDto);
        } catch (Throwable  throwable) {
            log.warning(throwable.getCause().getMessage());
        }
    }


}

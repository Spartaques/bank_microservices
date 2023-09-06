package com.andriibashuk.loanservice.kafka;

import com.andriibashuk.loanservice.model.Application;
import com.andriibashuk.loanservice.service.LoanService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Log
@Component
public class ApplicationListener {
    private final ObjectMapper objectMapper;
    private final LoanService loanService;

    public ApplicationListener(LoanService loanService, ObjectMapper objectMapper) {
        this.loanService = loanService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(id = "loans", topics = "application", containerFactory = "kafkaListenerContainerFactory", groupId = "loans")
    public void listen(Message message, Acknowledgment acknowledgment, @Header(name = KafkaHeaders.RECEIVED_KEY, required = false) Integer key,
                       @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                       @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                       @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts) throws JsonProcessingException, InterruptedException {
        log.info("kafka message reseived: "+message.getPayload() );
        Application application =  objectMapper.readValue(message.getPayload().toString(), Application.class);
        if(!Objects.equals(application.getSourceOfMessage(), "changeStatus")) {
            return;
        }
        if(application.getStatus() != Application.Status.SIGNED) {
            return;
        }
        log.info(application.toString());
        loanService.create(application.getClient().getId(), application.getId(), application.getApprovedAmount());
        acknowledgment.acknowledge();
    }
}

package com.andriibashuk.tmservice.kafka;

import com.andriibashuk.tmservice.entity.Ticket;
import com.andriibashuk.tmservice.model.Application;
import com.andriibashuk.tmservice.service.TicketService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.java.Log;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Log
@Component
public class ApplicationListener {
    private final TicketService ticketService;
    private final ObjectMapper objectMapper;

    public ApplicationListener(TicketService ticketService, ObjectMapper objectMapper) {
        this.ticketService = ticketService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(id = "tm", topics = "application", containerFactory = "kafkaListenerContainerFactory", groupId = "tm")
    public void listen(Message message, Acknowledgment acknowledgment, @Header(name = KafkaHeaders.RECEIVED_KEY, required = false) Integer key,
                       @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                       @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                       @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts) throws JsonProcessingException, InterruptedException {
        log.info("kafka message reseived: "+message.getPayload() );
        Application application =  objectMapper.readValue(message.getPayload().toString(), Application.class);
        if(!Objects.equals(application.getSourceOfMessage(), "changeStatus")) {
            return;
        }
        if(application.getStatus() != Application.Status.APPROVED) {
            return;
        }
        Map<String, String> meta = new HashMap<>();
        meta.put("applicationId", String.valueOf(application.getId()));
        meta.put("requestedAmount", String.valueOf(application.getRequestedAmount()));
        meta.put("approvedAmount", String.valueOf(application.getApprovedAmount()));
        log.info(application.toString());
        ticketService.create(application.getClient().getId(), application.getClient().getPhone(), Ticket.Status.NEW, Ticket.Type.APPLICATION, meta);
        acknowledgment.acknowledge();
    }
}

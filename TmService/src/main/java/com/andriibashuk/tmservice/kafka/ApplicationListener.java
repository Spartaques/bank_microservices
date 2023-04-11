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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Log
@Component
public class ApplicationListener {
    private final TicketService ticketService;

    public ApplicationListener(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @KafkaListener(id = "tm", topics = "application", containerFactory = "kafkaListenerContainerFactory")
    public void listen(Message message, Acknowledgment acknowledgment, @Header(name = KafkaHeaders.RECEIVED_KEY, required = false) Integer key,
                       @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                       @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                       @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts) throws JsonProcessingException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        asyncMethod();
        Thread.sleep(1000);
        Application application =  objectMapper.readValue(message.getPayload().toString(), Application.class);
        Map<String, String> meta = new HashMap<>();
        meta.put("applicationId", String.valueOf(application.getId()));
        meta.put("requestedAmount", String.valueOf(application.getRequestedAmount()));
        meta.put("approvedAmount", String.valueOf(application.getApprovedAmount()));
        log.info(application.toString());
        ticketService.create(application.getClient().getId(), application.getClient().getPhone(), Ticket.Status.NEW, Ticket.Type.APPLICATION, meta);
        acknowledgment.acknowledge();
    }

    @Async
    public void asyncMethod() {
        log.info("async Method");
    }
}

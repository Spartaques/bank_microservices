package com.andriibashuk.tmservice.kafka;

import lombok.extern.java.Log;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Log
@Component
public class ApplicationListener {
    @KafkaListener(id = "tm", topics = "application", containerFactory = "kafkaListenerContainerFactory")
    public void listen(Message message, Acknowledgment acknowledgment) {
        log.info(message.getPayload().toString());
        acknowledgment.acknowledge();

    }
}

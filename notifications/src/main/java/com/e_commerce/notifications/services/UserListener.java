package com.e_commerce.notifications.services;

import com.e_commerce.notifications.dtos.EmailDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class UserListener {

    @KafkaListener(topics = "new-user", groupId = "users-group")
    public void sendEmail(String json) throws JsonProcessingException {
        log.info("Received message: {}", json);
        ObjectMapper mapper = new ObjectMapper();
        var message = mapper.readValue(json, EmailDto.class);
        System.out.println("Sending email to user " + message.name() + " at " + message.email());
    }
}

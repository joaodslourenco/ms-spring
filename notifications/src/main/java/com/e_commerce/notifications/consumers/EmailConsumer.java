package com.e_commerce.notifications.consumers;

import com.e_commerce.notifications.dtos.EmailDto;
import com.e_commerce.notifications.enums.EmailType;

import com.e_commerce.notifications.models.EmailModel;
import com.e_commerce.notifications.services.EmailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class EmailConsumer {
    private final EmailService emailService;

    @KafkaListener(topics = "new-user", groupId = "users-group")
    public void newUserListener(String json) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        var data = mapper.readValue(json, EmailDto.class);

        var emailModel = EmailModel.builder()
                .name(data.name())
                .emailRecipient(data.email())
                .emailType(EmailType.WELCOME)
                .build();

        emailService.sendEmail(emailModel, EmailType.WELCOME);

        log.info("Sending email to user {} at {}", data.name(), data.email());
    }

}

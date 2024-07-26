package com.e_commerce.notifications.services;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserListener {

    @KafkaListener(topics = "new-user", groupId = "users-group")
    public void sendEmail(String email) {
        System.out.println("Sending email to " + email);
    }
}

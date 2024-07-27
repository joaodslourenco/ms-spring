package com.e_commerce.notifications.utils;

import com.e_commerce.notifications.enums.EmailType;
import com.e_commerce.notifications.models.EmailModel;
import org.springframework.mail.SimpleMailMessage;

public class EmailContentBuilder {

    public static SimpleMailMessage build(EmailModel emailModel) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(emailModel.getEmailRecipient());
        mailMessage.setSubject(getSubject(emailModel.getEmailType()));
        mailMessage.setText(getContent(emailModel));

        return mailMessage;
    }

    private static String getSubject(EmailType emailType) {
        return switch (emailType) {
            case WELCOME -> "Welcome to E-commerce!";
            case ORDER_CONFIRMATION -> "Order Confirmation";
            default -> "Notification from E-commerce";
        };
    }

    private static String getContent(EmailModel emailModel) {
        return switch (emailModel.getEmailType()) {
            case WELCOME -> welcomeEmail(emailModel.getName());
            case ORDER_CONFIRMATION -> orderConfirmationEmail(emailModel.getName());
            default -> "Hello, this is a notification from E-commerce.";
        };
    }

    private static String welcomeEmail(String name) {
        return "Hello,\n\nWelcome to E-commerce, %s! We are glad to have you.\n\nThank you,\nE-commerce Team".formatted(name);
    }

    private static String orderConfirmationEmail(String name) {
        return "Hello, %s!\n\nYour order has been confirmed. We will notify you once it is shipped.\n\nThank you,\nE-commerce Team".formatted(name);
    }
}

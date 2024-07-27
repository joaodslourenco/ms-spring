package com.e_commerce.notifications.services;

import com.e_commerce.notifications.enums.EmailType;
import com.e_commerce.notifications.enums.StatusEmail;
import com.e_commerce.notifications.models.EmailModel;
import com.e_commerce.notifications.repositories.EmailRepository;
import com.e_commerce.notifications.utils.EmailContentBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Log4j2
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final EmailRepository emailRepository;


    public void sendEmail(EmailModel emailModel, EmailType emailType) {
        log.info("Sending email to user {}", emailModel.getEmailRecipient());
        try {
            var mailMessage = EmailContentBuilder.build(emailModel);
            javaMailSender.send(mailMessage);
            emailModel.setSentAt(LocalDateTime.now());
            emailModel.setEmailStatus(StatusEmail.SENT);
        } catch (MailException e) {
            emailModel.setEmailStatus(StatusEmail.ERROR);
        }
        emailRepository.save(emailModel);
    }


}

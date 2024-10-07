package com.maja.orderService.emails;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String emailUsernameSender;
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendConfirmationEmail(String receiver, Long orderId, String confirmationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(receiver);
        message.setSubject("Confirmation code for order number " + orderId);
        message.setText("Your confirmation code for order " + orderId + " is: " + confirmationCode);
        message.setFrom(emailUsernameSender);
        mailSender.send(message);
    }
}

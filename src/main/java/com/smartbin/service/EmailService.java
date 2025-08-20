package com.smartbin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.from:${spring.mail.username}}")
    private String fromAddress;

    public void send(String recipientsCsv, String subject, String body) {
        if (recipientsCsv == null || recipientsCsv.isBlank()) {
            log.warn("No recipients provided. Skip sending email");
            return;
        }
        String[] recipients = Arrays.stream(recipientsCsv.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);
        if (recipients.length == 0) {
            log.warn("No valid recipients after parsing. Skip sending email");
            return;
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(recipients);
        message.setSubject(subject);
        message.setText(body);
        try {
            mailSender.send(message);
            log.info("Sent email to {} recipients: {}", recipients.length, String.join(", ", recipients));
        } catch (Exception ex) {
            log.error("Failed to send email: {}", ex.getMessage(), ex);
        }
    }
}

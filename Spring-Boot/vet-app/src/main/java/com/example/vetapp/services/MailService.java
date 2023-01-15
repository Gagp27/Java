package com.example.vetapp.services;

import com.example.vetapp.models.enums.EmailSubject;
import com.example.vetapp.exceptions.MailerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Value("${spring.mail.username}")
    private String mailOrigin;

    public void sendEmail(String userName, String email, String token, EmailSubject emailSubject) {
        String from = mailOrigin;
        String subject;
        String text;

        switch (emailSubject) {

            case REGISTER:
                subject = "Register Account";
                text = "<p>Hello " + userName + ", confirm your account " +
                        "<a href=\"" + frontendUrl + "/confirm/" + token + "\">Confirm Account</a>" +
                        "</p><p>If you don't create this account please ignore this message</p>";
                break;

            case RECOVER:
                subject = "Recover Account";
                text = "<p>Hello " + userName + ", recover your account " +
                        "<a href=\"" + frontendUrl + "/recover-account/" + token + "\">Recover Account</a>" +
                        "</p><p>If you did not request this change please ignore this message</p>";
                break;

            default:
                throw new MailerException("Invalid email subject");
        }


        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSubject(subject);
            helper.setTo(email);
            helper.setText(text, true);
            mailSender.send(message);

        } catch (MessagingException exception) {
            throw new MailerException(exception.getMessage());
        }
    }
}
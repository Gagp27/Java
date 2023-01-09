package com.example.uptask.service.implement;

import com.example.uptask.exceptions.MailerNotSendException;
import com.example.uptask.service.interfaces.IMailerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailService implements IMailerService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendRegisterEmail(Long userId, String userName, String email, String token) {
        String from = "UpTask@mailer.com";
        String subject = "Confirm you account";
        String frontendUrl = "http://localhost:3000";

        String text = "<p>Hello " + userName + ", confirm your account in UpTask " +
                "<a href=\"" + frontendUrl + "/confirm/" + token + "/" + userId + "\">Confirm Account</a>" +
                "</p><p>If you don't create this account please ignore this message</p>";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSubject(subject);
            helper.setTo(email);
            helper.setText(text, true);
            mailSender.send(message);

        } catch (MessagingException exception) {
            throw new MailerNotSendException("Can't send mail");
        }
    }

    @Override
    public void sendRecoverEmail(Long userId, String userName, String email, String token) {
        String from = "UpTask@mailer.com";
        String subject = "Recover you account";
        String frontendUrl = "http://localhost:3000";

        String text = "<p>Hello " + userName + ", recover your account in UpTask " +
                "<a href=\"" + frontendUrl + "/recover-account/" + token + "/" + userId + "\">Recover Account</a>" +
                "</p><p>If you did not request this change please ignore this message</p>";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSubject(subject);
            helper.setTo(email);
            helper.setText(text, true);
            mailSender.send(message);

        } catch (MessagingException exception) {
            throw new MailerNotSendException("Can't send mail");
        }
    }
}
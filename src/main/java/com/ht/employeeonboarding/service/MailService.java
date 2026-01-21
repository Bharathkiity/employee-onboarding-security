package com.ht.employeeonboarding.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmployeeCreationEmail(String to, String name, String username, String password) {
        String subject = "Welcome to HT Portal!";
        String body = "Hi " + name + ",\n\n" +
                      "Your employee profile has been created successfully.\n\n" +
                      "Login Details:\n" +
                      "Username (email): " + username + "\n" +
                      "Password: " + password + "\n\n" +
                      "Please change your password after login.\n\n" +
                      "Thanks,\nHT Admin Team";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        javaMailSender.send(message);
    }



     //✅ Send simple mail to a list of emails
    public void sendMailToEmployees(List<String> emails) {
        for (String email : emails) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Greetings from HT Portal!");
            message.setText("Hello,\n\nThis is a test bulk email.\n\nRegards,\nHT Admin Team");

            javaMailSender.send(message);
        }
    }
}


package com.todo.back.services;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Properties;

@Service
public class EmailService {

    public static void sendmail(String email) throws MessagingException, IOException {
        Dotenv dotenv = Dotenv.load();
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                return new jakarta.mail.PasswordAuthentication(dotenv.get("GM_ACC"), dotenv.get("GM_PASS"));
            }
        });

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(dotenv.get("GM_ACC"), false));

        String emailBody;
        try (InputStream inputStream = EmailService.class.getClassLoader().getResourceAsStream("templates/emailRegistration.html")) {
            assert inputStream != null;
            emailBody = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
        msg.setSubject("ToDo Successful Registration");
        msg.setContent(emailBody, "text/html");
        msg.setSentDate(new Date());

        Transport.send(msg);
    }
}

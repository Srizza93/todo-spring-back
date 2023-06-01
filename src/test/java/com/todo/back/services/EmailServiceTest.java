package com.todo.back.services;

import jakarta.mail.MessagingException;
import jakarta.mail.Transport;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.mockito.Mockito.doNothing;

@SpringBootTest
public class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private Transport transport;

    @Test
    public void shouldSendEmail() throws MessagingException, IOException {
        doNothing().when(transport);

        emailService.sendmail("toto@gmail.com");
    }
}

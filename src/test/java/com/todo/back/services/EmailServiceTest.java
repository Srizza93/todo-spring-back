package com.todo.back.services;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Properties;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class EmailServiceTest {

    @Test
    public void shouldSendEmail() throws MessagingException, IOException {
        EmailService spyEmailService = spy(new EmailService());

        doNothing().when(spyEmailService).sendMessage(any());

        spyEmailService.sendmail("toto@gmail.com");

        verify(spyEmailService, times(1)).sendmail(any());
    }
}

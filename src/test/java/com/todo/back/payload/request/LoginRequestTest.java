package com.todo.back.payload.request;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class LoginRequestTest {

    @InjectMocks
    LoginRequest loginRequest;

    @Test
    public void shouldSetGetUsername() {
        loginRequest.setUsername("toto");

        String username = loginRequest.getUsername();

        assertEquals(username, "toto");
    }

    @Test
    public void shouldSetGetPassword() {
        loginRequest.setPassword("password");

        String password = loginRequest.getPassword();

        assertEquals(password, "password");
    }
}

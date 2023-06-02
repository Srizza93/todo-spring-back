package com.todo.back.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthEntryPointJwtTest {

    @InjectMocks
    AuthEntryPointJwt authEntryPointJwt;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    AuthenticationException authException;

    @Test
    public void shouldCommence() throws IOException {
        String errorMessage = "Error: Unauthorized";
        String exceptionMessage = "Unauthorized error";

        doReturn(exceptionMessage).when(authException).getMessage();
        doNothing().when(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, errorMessage);

        authEntryPointJwt.commence(request, response, authException);

        verify(response, times(1)).sendError(HttpServletResponse.SC_UNAUTHORIZED, errorMessage);
    }
}

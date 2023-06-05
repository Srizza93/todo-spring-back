package com.todo.back.security.jwt;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.todo.back.security.services.UserDetailsImpl;
import io.jsonwebtoken.UnsupportedJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class JwtUtilsTest {

    @InjectMocks
    private JwtUtils jwtUtils;

    @Mock
    UserDetailsImpl userDetails;

    @Mock
    Authentication authentication;

    String authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJtb2NrIHRlc3QiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.HI10HgKnK_DL_V4Qy8oEvqKTq_QAMKmnkZDgc42_wtc";

    @BeforeEach
    public void init() {
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "mock");
    }

    @Test
    public void shouldGenerateToken() {
        when(authentication.getPrincipal()).thenReturn(userDetails);

        jwtUtils.generateJwtToken(authentication);

        verify(authentication, times(1)).getPrincipal();
    }

    @Test
    public void shouldGetUserNameFromJwtToken() {
        String jwt = jwtUtils.getUserNameFromJwtToken(authToken);

        assertEquals(jwt, "mock test");
    }

    @Test
    public void shouldValidateJwtToken() {
        Boolean validToken = jwtUtils.validateJwtToken(authToken);

        assertEquals(validToken, true);
    }

    @Test
    public void shouldntValidateJwtTokenSignatureException() {
        String jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJtb2NrIHRlc3QiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjIsImV4cCI6MH0._Jk7PCr-wUv9opF8j7PocbyZGB6ab452zEKW9EdYZ8c";

        Boolean validToken = jwtUtils.validateJwtToken(jwtToken);

        assertEquals(validToken, false);
    }

    @Test
    public void shouldntValidateJwtTokenMalformedJwtException() {
        String jwtToken = "invalid.token.malformed.exception";

        Boolean validToken = jwtUtils.validateJwtToken(jwtToken);

        assertEquals(validToken, false);
    }

    @Test
    public void shouldntValidateJwtTokenExpiredJwtException() {
        String jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJtb2NrIHRlc3QiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjIsImV4cCI6LTEwMDAwfQ.N9MFMZesNT37JaM7Stm2ENhFzYjIgi6WlpFGiR--j6M";

        Boolean validToken = jwtUtils.validateJwtToken(jwtToken);

        assertEquals(validToken, false);
    }

//    @Test
//    public void shouldntValidateJwtTokenUnsupportedJwtException() {
//        String jwtToken = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJtb2NrIHRlc3QiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwM/;,jJ9.OWMHs-4ABlrHyOZoh0xwU2yY1Z2TO7t_j_lvkDNxQlS_9v388WIh2PkSWkVoLm-v8YKGAcZs_PZ9C_js4qgSYA";
//
//        Boolean validToken = jwtUtils.validateJwtToken(jwtToken);
//
//        assertEquals(validToken, false);
//    }

    @Test
    public void shouldntValidateJwtTokenIllegalArgumentException() {
        String jwtToken = "";

        Boolean validToken = jwtUtils.validateJwtToken(jwtToken);

        assertEquals(validToken, false);
    }
}

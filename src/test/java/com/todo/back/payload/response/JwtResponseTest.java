package com.todo.back.payload.response;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class JwtResponseTest {

    JwtResponse jwtResponse = new JwtResponse("accessToken", 45543534L, "toto93", "toto@gmail.com", Set.of("USER_ROLE"));

    @Test
    public void shouldGetAccessToken() {
        String accessToken = jwtResponse.getAccessToken();

        assertEquals(accessToken, "accessToken");
    }

    @Test
    public void shouldSetAccessToken() {
        jwtResponse.setAccessToken("mock");

        String accessToken = jwtResponse.getAccessToken();

        assertEquals(accessToken, "mock");
    }

    @Test
    public void shouldSetGetTokenType() {
        jwtResponse.setTokenType("tokenType");

        String tokenType = jwtResponse.getTokenType();

        assertEquals(tokenType, "tokenType");
    }

    @Test
    public void shouldGetId() {
        Long id = jwtResponse.getId();

        assertEquals(id, 45543534L);
    }

    @Test
    public void shouldSetId() {
        jwtResponse.setId(12143534L);

        Long id = jwtResponse.getId();

        assertEquals(id, 12143534L);
    }

    @Test
    public void shouldGetUsername() {
        String username = jwtResponse.getUsername();

        assertEquals(username, "toto93");
    }

    @Test
    public void shouldSetUsername() {
        jwtResponse.setUsername("mock");

        String username = jwtResponse.getUsername();

        assertEquals(username, "mock");
    }

    @Test
    public void shouldGetEmail() {
        String email = jwtResponse.getEmail();

        assertEquals(email, "toto@gmail.com");
    }

    @Test
    public void shouldSetEmail() {
        jwtResponse.setEmail("mock");

        String email = jwtResponse.getEmail();

        assertEquals(email, "mock");
    }

    @Test
    public void shouldGetRoles() {
        Set<String> roles = jwtResponse.getRoles();

        assertEquals(roles, Set.of("USER_ROLE"));
    }

}

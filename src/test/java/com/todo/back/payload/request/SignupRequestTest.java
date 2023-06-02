package com.todo.back.payload.request;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SignupRequestTest {

    SignupRequest signupRequest = new SignupRequest("toto93", "toto", "tutu", "toto@gmail.com", "password");

    @Test
    public void shouldGetUsername() {
        String username = signupRequest.getUsername();

        assertEquals(username, "toto93");
    }

    @Test
    public void shouldSetUsername() {
        signupRequest.setUsername("mock");

        String username = signupRequest.getUsername();

        assertEquals(username, "mock");
    }

    @Test
    public void shouldGetName() {
        String name = signupRequest.getName();

        assertEquals(name, "toto");
    }

    @Test
    public void shouldSetName() {
        signupRequest.setName("mock");

        String name = signupRequest.getName();

        assertEquals(name, "mock");
    }

    @Test
    public void shouldGetSurame() {
        String surname = signupRequest.getSurname();

        assertEquals(surname, "tutu");
    }

    @Test
    public void shouldSetSurame() {
        signupRequest.setSurname("mock");

        String surname = signupRequest.getSurname();

        assertEquals(surname, "mock");
    }

    @Test
    public void shouldGetEmail() {
        String email = signupRequest.getEmail();

        assertEquals(email, "toto@gmail.com");
    }

    @Test
    public void shouldSetEmail() {
        signupRequest.setEmail("mock");

        String email = signupRequest.getEmail();

        assertEquals(email, "mock");
    }

    @Test
    public void shouldGetPassword() {
        String password = signupRequest.getPassword();

        assertEquals(password, "password");
    }

    @Test
    public void shouldSetPassword() {
        signupRequest.setPassword("mock");

        String password = signupRequest.getPassword();

        assertEquals(password, "mock");
    }

    @Test
    public void shouldSetGetRoles() {
        signupRequest.setRole(Collections.singleton("USER_ROLE"));

        Set<String> roles = signupRequest.getRoles();

        assertEquals(roles, Collections.singleton("USER_ROLE"));
    }
}

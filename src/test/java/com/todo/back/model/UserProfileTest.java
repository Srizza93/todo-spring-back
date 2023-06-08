package com.todo.back.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserProfileTest {

    UserProfile userProfile = new UserProfile("toto93", "toto", "tutu", "toto@gmail.com", "password");

    @Test
    public void shouldSetGetId() {
        userProfile.setId("1");

        String id = userProfile.getId();

        assertEquals(id, "1");
    }

    @Test
    public void shouldGetUsername() {
        String username = userProfile.getUsername();

        assertEquals(username, "toto93");
    }

    @Test
    public void shouldSetUsername() {
        userProfile.setUsername("mock");

        String username = userProfile.getUsername();

        assertEquals(username, "mock");
    }

    @Test
    public void shouldGetName() {
        String name = userProfile.getName();

        assertEquals(name, "toto");
    }

    @Test
    public void shouldSetName() {
        userProfile.setName("mock");

        String name = userProfile.getName();

        assertEquals(name, "mock");
    }

    @Test
    public void shouldGetSurame() {
        String surname = userProfile.getSurname();

        assertEquals(surname, "tutu");
    }

    @Test
    public void shouldSetSurname() {
        userProfile.setSurname("mock");

        String surname = userProfile.getSurname();

        assertEquals(surname, "mock");
    }

    @Test
    public void shouldGetEmail() {
        String email = userProfile.getEmail();

        assertEquals(email, "toto@gmail.com");
    }

    @Test
    public void shouldSetEmail() {
        userProfile.setEmail("mock");

        String email = userProfile.getEmail();

        assertEquals(email, "mock");
    }

    @Test
    public void shouldGetPassword() {
        String password = userProfile.getPassword();

        assertEquals(password, "password");
    }

    @Test
    public void shouldSetPassword() {
        userProfile.setPassword("mock");

        String password = userProfile.getPassword();

        assertEquals(password, "mock");
    }

    @Test
    public void shouldSetGetRoles() {
        Role userRole = new Role(ERole.ROLE_USER);

        userProfile.setRoles(Set.of(userRole));

        Set<Role> roles = userProfile.getRoles();

        assertEquals(roles, Set.of(userRole));
    }
}
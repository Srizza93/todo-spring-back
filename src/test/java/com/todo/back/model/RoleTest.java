package com.todo.back.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RoleTest {

    Role role = new Role(ERole.ROLE_USER);

    @Test
    public void shouldSetGetId() {
        role.setId("1");

        String id = role.getId();

        assertEquals(id, "1");
    }

    @Test
    public void shouldGetName() {
        ERole name = role.getName();

        assertEquals(name, ERole.ROLE_USER);
    }

    @Test
    public void shouldSetName() {
        role.setName(ERole.ROLE_ADMIN);

        ERole name = role.getName();

        assertEquals(name, ERole.ROLE_ADMIN);
    }

}
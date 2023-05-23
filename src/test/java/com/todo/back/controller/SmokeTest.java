package com.todo.back.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SmokeTest {

    @Autowired
    private UserController userController;

    @Autowired
    private TodoItemController todoItemController;

    @Test
    public void contextLoads() throws Exception {
        assertThat(userController).isNotNull();
        assertThat(todoItemController).isNotNull();
    }
}

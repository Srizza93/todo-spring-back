package com.todo.back;

import com.todo.back.controller.TodoItemController;
import com.todo.back.controller.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BackApplicationTests {

	@Autowired
	private UserController userController;

	@Autowired
	private TodoItemController todoItemController;

	@Test
	void contextLoads() {
		assertThat(userController).isNotNull();
		assertThat(todoItemController).isNotNull();
	}

}

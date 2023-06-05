package com.todo.back;

import com.todo.back.controller.TodoItemController;
import com.todo.back.controller.UserController;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
class BackApplicationTests {

	@Autowired
	private UserController userController;

	@Autowired
	private TodoItemController todoItemController;

	@Test
	void contextLoads() {
		try {
			BackApplication.main(new String[0]);
		} catch (Exception e) {
			fail("BackApplication couldn't be started");
		}

		assertThat(userController).isNotNull();
		assertThat(todoItemController).isNotNull();
	}

}

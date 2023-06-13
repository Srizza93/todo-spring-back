package com.todo.back.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TodoItemTest {

    TodoItem todoItem = new TodoItem(111233332L, 1111344533L, "Hello World", new Date(167721600000L), new Date(167721500000L));

    @Test
    public void shouldGetId() {
        Long id = todoItem.getId();

        assertEquals(id, 111233332L);
    }

    @Test
    public void shouldSetId() {
        todoItem.setId(222233332L);

        Long id = todoItem.getId();

        assertEquals(id, 222233332L);
    }

    @Test
    public void shouldGetUserId() {
        Long userId = todoItem.getUserId();

        assertEquals(userId, 1111344533L);
    }

    @Test
    public void shouldSetUserId() {
        todoItem.setUserId(2222344533L);

        Long userId = todoItem.getUserId();

        assertEquals(userId, 2222344533L);
    }

    @Test
    public void shouldGetContent() {
        String content = todoItem.getContent();

        assertEquals(content, "Hello World");
    }

    @Test
    public void shouldSetContent() {
        todoItem.setContent("mock");

        String content = todoItem.getContent();

        assertEquals(content, "mock");
    }

    @Test
    public void shouldGetDue() {
        Date due = todoItem.getDue();

        assertEquals(due, new Date(167721600000L));
    }

    @Test
    public void shouldSetDue() {
        todoItem.setDue(new Date(167721700000L));

        Date due = todoItem.getDue();

        assertEquals(due, new Date(167721700000L));
    }

    @Test
    public void shouldGetCreated() {
        Date created = todoItem.getCreated();

        assertEquals(created, new Date(167721500000L));
    }

    @Test
    public void shouldSetCreated() {
        todoItem.setCreated(new Date(167721400000L));

        Date created = todoItem.getCreated();

        assertEquals(created, new Date(167721400000L));
    }

    @Test
    public void shouldSetGetDone() {
        todoItem.setDone(true);

        Boolean done = todoItem.getDone();

        assertEquals(done, true);
    }
}

package com.todo.back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.back.dto.TodoDto;
import com.todo.back.model.TodoItem;
import com.todo.back.services.TodoService;
import com.todo.back.services.UserServiceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(username="admin",roles={"USER","ADMIN"})
public class TodoItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    private final Long mockId = 33333332L;

    TodoItem todoItem1 = new TodoItem(111233332L, 22233332L, "Todo 1", null, null);

    TodoItem todoItem2 = new TodoItem(111233332L, 22233332L, "Todo 2", null, null);

    private final TodoDto todoItemDto = new TodoDto();

    CollectionModel<EntityModel<TodoItem>> todos = CollectionModel.of(
            Arrays.asList(
                    EntityModel.of(todoItem1),
                    EntityModel.of(todoItem2)
            )
    );

    @Test
    public void shouldGetAllTodos() throws Exception {
        when(todoService.todos()).thenReturn(todos);

        MvcResult mockMvc1 = this.mockMvc.perform(get("/todos"))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        verify(todoService, times(1)).todos();

        assertEquals(mockMvc1.getResponse().getContentType(), "application/hal+json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/todos");
    }

    @Test
    public void shouldntGetAllTodosUserServiceException() throws Exception {
        doThrow(UserServiceException.class).when(todoService).todos();

        MvcResult mockMvc1 = this.mockMvc.perform(get("/todos"))
                .andDo(print()).andExpect(status().isInternalServerError()).andReturn();

        verify(todoService, times(1)).todos();

        assertEquals(mockMvc1.getResponse().getContentType(), "application/json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/todos");
    }

    @Test
    public void shouldGetAllTodayTodos() throws Exception {
        when(todoService.today(mockId)).thenReturn(todos);

        MvcResult mockMvc1 = this.mockMvc.perform(get("/todos/TODAY/{userId}", mockId))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        verify(todoService, times(1)).today(mockId);

        assertEquals(mockMvc1.getResponse().getContentType(), "application/hal+json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/todos/TODAY/33333332");
    }

    @Test
    public void shouldntGetAllTodayTodosUserServiceException() throws Exception {
        doThrow(UserServiceException.class).when(todoService).today(mockId);

        MvcResult mockMvc1 = this.mockMvc.perform(get("/todos/TODAY/{userId}", mockId))
                .andDo(print()).andExpect(status().isInternalServerError()).andReturn();

        verify(todoService, times(1)).today(mockId);

        assertEquals(mockMvc1.getResponse().getContentType(), "application/json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/todos/TODAY/33333332");
    }

    @Test
    public void shouldGetAllInboxTodos() throws Exception {
        when(todoService.inbox(mockId)).thenReturn(todos);

        MvcResult mockMvc1 = this.mockMvc.perform(get("/todos/INBOX/{userId}", mockId))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        verify(todoService, times(1)).inbox(mockId);

        assertEquals(mockMvc1.getResponse().getContentType(), "application/hal+json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/todos/INBOX/33333332");
    }

    @Test
    public void shouldntGetAllInboxTodosUserServiceException() throws Exception {
        doThrow(UserServiceException.class).when(todoService).inbox(mockId);

        MvcResult mockMvc1 = this.mockMvc.perform(get("/todos/INBOX/{userId}", mockId))
                .andDo(print()).andExpect(status().isInternalServerError()).andReturn();

        verify(todoService, times(1)).inbox(mockId);

        assertEquals(mockMvc1.getResponse().getContentType(), "application/json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/todos/INBOX/33333332");
    }

    @Test
    public void shouldGetAllDoneTodos() throws Exception {
        when(todoService.done(mockId)).thenReturn(todos);

        MvcResult mockMvc1 = this.mockMvc.perform(get("/todos/DONE/{userId}", mockId))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        verify(todoService, times(1)).done(mockId);

        assertEquals(mockMvc1.getResponse().getContentType(), "application/hal+json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/todos/DONE/33333332");
    }

    @Test
    public void shouldntGetAllDoneTodosUserServiceException() throws Exception {
        doThrow(UserServiceException.class).when(todoService).done(mockId);

        MvcResult mockMvc1 = this.mockMvc.perform(get("/todos/DONE/{userId}", mockId))
                .andDo(print()).andExpect(status().isInternalServerError()).andReturn();

        verify(todoService, times(1)).done(mockId);

        assertEquals(mockMvc1.getResponse().getContentType(), "application/json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/todos/DONE/33333332");
    }

    @Test
    public void shouldAddANewTodo() throws Exception {
        when(todoService.addTodo(any(TodoDto.class))).thenReturn(todoItem1);

        MvcResult mockMvc1 = this.mockMvc.perform(post("/todos")
                        .content(new ObjectMapper().writeValueAsString(todoItemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        verify(todoService, times(1)).addTodo(any(TodoDto.class));

        assertEquals(mockMvc1.getResponse().getContentType(), "application/json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/todos");
    }

    @Test
    public void shouldntAddATodoUserServiceException() throws Exception {
        doThrow(UserServiceException.class).when(todoService).addTodo(any(TodoDto.class));

        MvcResult mockMvc1 = this.mockMvc.perform(post("/todos")
                        .content(new ObjectMapper().writeValueAsString(todoItemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isInternalServerError()).andReturn();

        verify(todoService, times(1)).addTodo(any(TodoDto.class));

        assertEquals(mockMvc1.getResponse().getContentType(), "application/json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/todos");
    }

    @Test
    public void shouldEditATodo() throws Exception {
        when(todoService.editTodoStatus(any(TodoDto.class))).thenReturn(todoItem1);

        MvcResult mockMvc1 = this.mockMvc.perform(put("/todos")
                        .content(new ObjectMapper().writeValueAsString(todoItemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        verify(todoService, times(1)).editTodoStatus(any(TodoDto.class));

        assertEquals(mockMvc1.getResponse().getContentType(), "application/json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/todos");
    }

    @Test
    public void shouldntEditATodo() throws Exception {
        doThrow(UserServiceException.class).when(todoService).editTodoStatus(any(TodoDto.class));

        MvcResult mockMvc1 = this.mockMvc.perform(put("/todos")
                        .content(new ObjectMapper().writeValueAsString(todoItemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isInternalServerError()).andReturn();

        verify(todoService, times(1)).editTodoStatus(any(TodoDto.class));

        assertEquals(mockMvc1.getResponse().getContentType(), "application/json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/todos");
    }

    @Test
    public void shouldDeleteATodo() throws Exception {
        when(todoService.deleteTodo(mockId)).thenReturn(ResponseEntity.ok(null));

        MvcResult mockMvc1 = this.mockMvc.perform(delete("/todos/{id}", mockId))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        verify(todoService, times(1)).deleteTodo(mockId);

        assertEquals(mockMvc1.getResponse().getContentType(), "application/json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/todos/33333332");
    }

    @Test
    public void shouldntDeleteATodo() throws Exception {
        doThrow(UserServiceException.class).when(todoService).deleteTodo(mockId);

        MvcResult mockMvc1 = this.mockMvc.perform(delete("/todos/{id}", mockId))
                .andDo(print()).andExpect(status().isInternalServerError()).andReturn();

        verify(todoService, times(1)).deleteTodo(mockId);

        assertEquals(mockMvc1.getResponse().getContentType(), "application/json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/todos/33333332");
    }

}

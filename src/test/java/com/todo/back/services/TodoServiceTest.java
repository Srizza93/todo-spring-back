package com.todo.back.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.back.dto.TodoDto;
import com.todo.back.model.TodoItem;
import com.todo.back.repository.ItemRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(username="admin",roles={"USER","ADMIN"})
public class TodoServiceTest {

    @Autowired
    private MockMvc mockMvc;

    TodoItem todoItem1 = new TodoItem("1", "user1", "Todo 1",  null, null);

    TodoItem todoItem2 = new TodoItem("2", "user2", "Unlock your potential, embrace challenges, learn from failures, and grow into the best version of yourself!", null, null);

    TodoDto todoDto = new TodoDto();

    List<TodoItem> todoItemList = Arrays.asList(todoItem1, todoItem2);

    private final ItemRepository todoItemRepository = mock(ItemRepository.class);

    @InjectMocks
    private TodoService todoService;

    private final TodoService todoServiceMock = mock(TodoService.class);

    private final String mockId = "123";

    @Test
    public void shouldGetAllTodos() {
        when(todoItemRepository.findAll()).thenReturn(todoItemList);

        CollectionModel<EntityModel<TodoItem>> todos = todoService.todos();

        verify(todoItemRepository, times(1)).findAll();

        assertEquals(todoItemList.size(), todos.getContent().size());
    }

    @Test
    public void shouldntGetAllTodosUserServiceException() {
        given(todoService.todos()).willThrow(UserServiceException.class);

        assertThrows(UserServiceException.class, () -> todoService.todos());
    }

    @Test
    public void shouldGetAllTodayTodos() {
        LocalDate localDate = LocalDate.now();
        LocalDateTime today = localDate.atTime(LocalTime.MAX);

        when(todoItemRepository.findByUserIdAndDoneAndDueLessThanEqual(mockId, false, today)).thenReturn(todoItemList);

        CollectionModel<EntityModel<TodoItem>> todos = todoService.today(mockId);

        verify(todoItemRepository, times(1)).findByUserIdAndDoneAndDueLessThanEqual(mockId, false, today);

        assertEquals(todoItemList.size(), todos.getContent().size());
    }

    @Test
    public void shouldntGetAllTodayTodosUserServiceException() {
        given(todoService.today(mockId)).willThrow(UserServiceException.class);

        assertThrows(UserServiceException.class, () -> todoService.today(mockId));
    }

    @Test
    public void shouldGetAllInboxTodos() {
        when(todoItemRepository.findByUserIdAndDone(mockId, false)).thenReturn(todoItemList);

        CollectionModel<EntityModel<TodoItem>> todos = todoService.inbox(mockId);

        verify(todoItemRepository, times(1)).findByUserIdAndDone(mockId, false);

        assertEquals(todoItemList.size(), todos.getContent().size());
    }

    @Test
    public void shouldntGetAllInboxTodosUserServiceException() {
        given(todoService.inbox(mockId)).willThrow(UserServiceException.class);

        assertThrows(UserServiceException.class, () -> todoService.inbox(mockId));
    }

    @Test
    public void shouldGetAllDoneTodos() {
        when(todoItemRepository.findByUserIdAndDone(mockId, true)).thenReturn(todoItemList);

        CollectionModel<EntityModel<TodoItem>> todos = todoService.done(mockId);

        verify(todoItemRepository, times(1)).findByUserIdAndDone(mockId, true);

        assertEquals(todoItemList.size(), todos.getContent().size());
    }

    @Test
    public void shouldntGetAllDoneTodosUserServiceException() {
        given(todoService.done(mockId)).willThrow(UserServiceException.class);

        assertThrows(UserServiceException.class, () -> todoService.done(mockId));
    }

    @Test
    public void shouldAddATodo() {
        TodoItem todoItem = new TodoItem(null, null, todoItem1.getContent(), null, null);
        TodoDto todoDto = new TodoDto();
        todoDto.setContent(todoItem.getContent());

        ArgumentCaptor<TodoItem> todoItemCaptor = ArgumentCaptor.forClass(TodoItem.class);
        when(todoItemRepository.save(todoItemCaptor.capture())).thenReturn(todoItem);

        todoService.addTodo(todoDto);

        TodoItem capturedTodoItem = todoItemCaptor.getValue();
        assertEquals(todoItem.getContent(), capturedTodoItem.getContent());

        verify(todoItemRepository, times(1)).save(capturedTodoItem);
    }

    @Test
    public void shouldntAddATodoWithLongContent() {
        TodoItem todoItem = new TodoItem(null, null, todoItem2.getContent(), null, null);
        TodoDto todoDto = new TodoDto();
        todoDto.setContent(todoItem.getContent());

        assertThrows(IllegalArgumentException.class, () -> todoService.addTodo(todoDto));
    }

    @Test
    public void shouldntAddATodoUserServiceException() {
        doThrow(new UserServiceException("Failed to add a new todo")).when(todoServiceMock).addTodo(any(TodoDto.class));

        assertThrows(UserServiceException.class, () -> todoServiceMock.addTodo(todoDto));
    }

    @Test
    public void shouldChangeATodo() throws Exception {
        TodoItem todo = new TodoItem("1", "123", "Hello World", new Date(), new Date());

        MvcResult mockMvc1 = this.mockMvc.perform(put("/todos").content(new ObjectMapper().writeValueAsString(todo))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/todos");
    }

    @Test
    public void shouldntChangeATodoNullTodo() throws Exception {
        MvcResult mockMvc1 = this.mockMvc.perform(put("/todos"))
                .andDo(print()).andExpect(status().isBadRequest()).andReturn();

        assertNull(mockMvc1.getResponse().getContentType());
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/todos");
    }

//    @Test
//    public void shouldntChangeATodoUserServiceException() throws Exception {
//        TodoItem todo = new TodoItem("1", "123", "Hello World", new Date(), new Date());
//
//        doThrow(UserServiceException.class).when(todoItemRepository).findById(todo.getId());
//
//        MvcResult mockMvc1 = this.mockMvc.perform(put("/todos").content(new ObjectMapper().writeValueAsString(todo))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print()).andExpect(status().isInternalServerError()).andReturn();
//
//        String responseBody = mockMvc1.getResponse().getContentAsString();
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode responseJson = objectMapper.readTree(responseBody);
//        String errorMessage = responseJson.path("message").asText();
//
//        assertEquals(mockMvc1.getResponse().getContentType(), "application/json");
//        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
//        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/todos");
//        assertEquals(errorMessage, "Failed to edit the todo status");
//    }

    @Test
    public void shouldDeleteATodo() throws Exception {
        MvcResult mockMvc1 = this.mockMvc.perform(delete("/todos/{id}", mockId))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        assertEquals(mockMvc1.getResponse().getContentType(), "application/json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/todos/123");
    }

    @Test
    public void shouldntDeleteATodoNullId() throws Exception {
        MvcResult mockMvc1 = this.mockMvc.perform(delete("/todos/{id}", (String) null))
                .andDo(print()).andExpect(status().isNotFound()).andReturn();

        assertNull(mockMvc1.getResponse().getContentType());
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/todos/");
    }

//    @Test
//    public void shouldntDeleteATodoUserServiceException() throws Exception {
//        doThrow(UserServiceException.class).when(todoItemRepository).deleteById(mockId);
//
//        MvcResult mockMvc1 = this.mockMvc.perform(delete("/todos/{id}", mockId))
//                .andDo(print()).andExpect(status().isInternalServerError()).andReturn();
//
//        String responseBody = mockMvc1.getResponse().getContentAsString();
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode responseJson = objectMapper.readTree(responseBody);
//        String errorMessage = responseJson.path("message").asText();
//
//        assertEquals(mockMvc1.getResponse().getContentType(), "application/json");
//        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
//        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/todos/123");
//        assertEquals(errorMessage, "Failed to delete the todo");
//    }
}

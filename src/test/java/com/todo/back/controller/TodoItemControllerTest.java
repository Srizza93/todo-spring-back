package com.todo.back.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.back.model.TodoItem;
import com.todo.back.repository.ItemRepository;
import com.todo.back.services.UserServiceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(username="admin",roles={"USER","ADMIN"})
public class TodoItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private ItemRepository todoItemRepository;

    private final String mockId = "123";

    @Test
    public void shouldGetAllTodos() throws Exception {
        MvcResult mockMvc1 = this.mockMvc.perform(get("/todos"))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        assertEquals(mockMvc1.getResponse().getContentType(), "application/hal+json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/todos");
    }

    @Test
    public void shouldntGetAllTodosError() throws Exception {
        doThrow(UserServiceException.class);

        MvcResult mockMvc1 = this.mockMvc.perform(get("/todos"))
                .andDo(print()).andExpect(status().isInternalServerError()).andReturn();

        String responseBody = mockMvc1.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(responseBody);
        String errorMessage = responseJson.path("message").asText();

        assertEquals(mockMvc1.getResponse().getContentType(), "application/json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/todos");
        assertEquals(errorMessage, "Failed to fetch todos");
    }

    @Test
    public void shouldGetAllTodayTodos() throws Exception {
        MvcResult mockMvc1 = this.mockMvc.perform(get("/todos/TODAY/{userId}", mockId))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        assertEquals(mockMvc1.getResponse().getContentType(), "application/hal+json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/todos/TODAY/123");
    }

    @Test
    public void shouldntGetAllTodayTodosError() throws Exception {
        doThrow(UserServiceException.class);

        MvcResult mockMvc1 = this.mockMvc.perform(get("/todos/TODAY/{userId}", mockId))
                .andDo(print()).andExpect(status().isInternalServerError()).andReturn();

        String responseBody = mockMvc1.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(responseBody);
        String errorMessage = responseJson.path("message").asText();

        assertEquals(mockMvc1.getResponse().getContentType(), "application/json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/todos/TODAY/123");
        assertEquals(errorMessage, "Failed to fetch today's todos");
    }

    @Test
    public void shouldGetAllInboxTodos() throws Exception {
        MvcResult mockMvc1 = this.mockMvc.perform(get("/todos/INBOX/{userId}", mockId))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        assertEquals(mockMvc1.getResponse().getContentType(), "application/hal+json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/todos/INBOX/123");
    }

    @Test
    public void shouldntGetAllInboxTodosError() throws Exception {
        doThrow(UserServiceException.class);

        MvcResult mockMvc1 = this.mockMvc.perform(get("/todos/INBOX/{userId}", mockId))
                .andDo(print()).andExpect(status().isInternalServerError()).andReturn();

        String responseBody = mockMvc1.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(responseBody);
        String errorMessage = responseJson.path("message").asText();

        assertEquals(mockMvc1.getResponse().getContentType(), "application/json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/todos/INBOX/123");
        assertEquals(errorMessage, "Failed to fetch inbox's todos");
    }

    @Test
    public void shouldGetAllDoneTodos() throws Exception {
        MvcResult mockMvc1 = this.mockMvc.perform(get("/todos/DONE/{userId}", mockId))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        assertEquals(mockMvc1.getResponse().getContentType(), "application/hal+json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/todos/DONE/123");
    }

    @Test
    public void shouldntGetAllDoneTodosError() throws Exception {
        doThrow(UserServiceException.class);

        MvcResult mockMvc1 = this.mockMvc.perform(get("/todos/DONE/{userId}", mockId))
                .andDo(print()).andExpect(status().isInternalServerError()).andReturn();

        String responseBody = mockMvc1.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(responseBody);
        String errorMessage = responseJson.path("message").asText();

        assertEquals(mockMvc1.getResponse().getContentType(), "application/json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/todos/DONE/123");
        assertEquals(errorMessage, "Failed to fetch done todos");
    }

    @Test
    public void shouldAddATodo() throws Exception {
        TodoItem todo = new TodoItem("1", "123", "Hello World", new Date(), new Date());

        MvcResult mockMvc1 = this.mockMvc.perform(post("/todos")
                .content(new ObjectMapper().writeValueAsString(todo))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        assertNull(mockMvc1.getResponse().getContentType());
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/todos");
    }

    @Test
    public void shouldntAddATodoWithLongContent() throws Exception {
        TodoItem todo = new TodoItem("1", "123", "The quick brown fox jumps over the lazy dogs. The quick brown fox jumps over the lazy dogs. The quick brown fox jumps over the lazy dogs.", new Date(), new Date());

        MvcResult mockMvc1 = this.mockMvc.perform(post("/todos")
                        .content(new ObjectMapper().writeValueAsString(todo))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isInternalServerError()).andReturn();

        String responseBody = mockMvc1.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(responseBody);
        String errorMessage = responseJson.path("message").asText();

        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/todos");
        assertEquals(errorMessage, "The content is too long");
    }

    @Test
    public void shouldntAddATodoUserServiceException() throws Exception {
        doThrow(UserServiceException.class);

        TodoItem todo = new TodoItem("1", "123", "Hello World!", new Date(), new Date());

        MvcResult mockMvc1 = this.mockMvc.perform(post("/todos")
                        .content(new ObjectMapper().writeValueAsString(todo))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isInternalServerError()).andReturn();

        String responseBody = mockMvc1.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(responseBody);
        String errorMessage = responseJson.path("message").asText();

        assertEquals(mockMvc1.getResponse().getContentType(), "application/json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/todos");
        assertEquals(errorMessage, "Failed to add a new todo");
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

    @Test
    public void shouldntChangeATodoUserServiceException() throws Exception {
        doThrow(UserServiceException.class);

        TodoItem todo = new TodoItem("1", "123", "Hello World", new Date(), new Date());

        MvcResult mockMvc1 = this.mockMvc.perform(put("/todos").content(new ObjectMapper().writeValueAsString(todo))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isInternalServerError()).andReturn();

        String responseBody = mockMvc1.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(responseBody);
        String errorMessage = responseJson.path("message").asText();

        assertEquals(mockMvc1.getResponse().getContentType(), "application/json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/todos");
        assertEquals(errorMessage, "Failed to edit the todo status");
    }

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

    @Test
    public void shouldntDeleteATodoUserServiceException() throws Exception {
        doThrow(UserServiceException.class);

        MvcResult mockMvc1 = this.mockMvc.perform(delete("/todos/{id}", mockId))
                .andDo(print()).andExpect(status().isInternalServerError()).andReturn();

        String responseBody = mockMvc1.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(responseBody);
        String errorMessage = responseJson.path("message").asText();

        assertEquals(mockMvc1.getResponse().getContentType(), "application/json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/todos/123");
        assertEquals(errorMessage, "Failed to delete the todo");
    }
}

package com.todo.back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.back.model.TodoItem;
import com.todo.back.repository.ItemRepository;
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
    public void shouldGetAllTodayTodos() throws Exception {
        MvcResult mockMvc1 = this.mockMvc.perform(get("/todos/TODAY/{userId}", mockId))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        assertEquals(mockMvc1.getResponse().getContentType(), "application/hal+json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/todos/TODAY/123");
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
    public void shouldGetAllDoneTodos() throws Exception {
        MvcResult mockMvc1 = this.mockMvc.perform(get("/todos/DONE/{userId}", mockId))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        assertEquals(mockMvc1.getResponse().getContentType(), "application/hal+json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/todos/DONE/123");
    }

    @Test
    public void shouldAddATodo() throws Exception {
        TodoItem todo = new TodoItem("1", "123", "Hello World", new Date(), new Date());

        MvcResult mockMvc1 = this.mockMvc.perform(post("/todos")
                .content(new ObjectMapper().writeValueAsString(todo))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/todos");
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
    public void shouldDeleteATodo() throws Exception {
        MvcResult mockMvc1 = this.mockMvc.perform(delete("/todos/{id}", mockId))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        assertEquals(mockMvc1.getResponse().getContentType(), "application/json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/todos/123");
    }
}

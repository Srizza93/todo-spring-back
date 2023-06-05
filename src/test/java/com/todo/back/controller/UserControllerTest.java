package com.todo.back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.back.model.UserProfile;
import com.todo.back.payload.request.LoginRequest;
import com.todo.back.payload.request.SignupRequest;
import com.todo.back.services.UserService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(username="admin",roles={"USER","ADMIN"})
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    UserProfile user1 = new UserProfile("toto93", "toto", "tutu", "toto@gmail.com", "A!1aaaaaaaaa");

    UserProfile user2 = new UserProfile("!", "!", "!", "!", "!");

    CollectionModel<EntityModel<UserProfile>> users = CollectionModel.of(
            Arrays.asList(
                    EntityModel.of(user1),
                    EntityModel.of(user2)
            )
    );

    @Test
    public void shouldGetAllUsers() throws Exception {
        when(userService.users()).thenReturn(users);

        MvcResult mockMvc1 = this.mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        verify(userService, times(1)).users();

        assertEquals(mockMvc1.getResponse().getContentType(), "application/hal+json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/users");
    }

    @Test
    public void shouldntGetAllUsers() throws Exception {
        doThrow(UserServiceException.class).when(userService).users();

        MvcResult mockMvc1 = this.mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isInternalServerError()).andReturn();

        verify(userService, times(1)).users();

        assertEquals(mockMvc1.getResponse().getContentType(), "application/json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/users");
    }

    @Test
    public void shouldLogin() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(user1.getUsername());
        loginRequest.setPassword(user1.getPassword());

        when(userService.login(any(LoginRequest.class))).thenReturn(ResponseEntity.ok(null));

        MvcResult mockMvc1 = this.mockMvc.perform(post("/login")
                        .content(new ObjectMapper().writeValueAsString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        verify(userService, times(1)).login(any(LoginRequest.class));

        assertEquals(mockMvc1.getResponse().getContentType(), "application/json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/login");
    }

    @Test
    public void shouldntLoginInternalServerError() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(user1.getUsername());
        loginRequest.setPassword(user1.getPassword());

        doThrow(UserServiceException.class).when(userService).login(any(LoginRequest.class));

        MvcResult mockMvc1 = this.mockMvc.perform(post("/login")
                        .content(new ObjectMapper().writeValueAsString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError()).andReturn();

        verify(userService, times(1)).login(any(LoginRequest.class));

        assertEquals(mockMvc1.getResponse().getContentType(), "application/json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/login");
    }

    @Test
    public void shouldntLoginIllegalArgumentException() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(user1.getUsername());
        loginRequest.setPassword(user1.getPassword());

        doThrow(IllegalArgumentException.class).when(userService).login(any(LoginRequest.class));

        MvcResult mockMvc1 = this.mockMvc.perform(post("/login")
                        .content(new ObjectMapper().writeValueAsString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest()).andReturn();

        verify(userService, times(1)).login(any(LoginRequest.class));

        assertEquals(mockMvc1.getResponse().getContentType(), "application/json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/login");
    }

    @Test
    public void shouldSignup() throws Exception {
        SignupRequest signupRequest =
                new SignupRequest("toto123", "toto", "tutu", "toto@gmail.com", "Aa!1aaaaaaaa");

        doNothing().when(userService).signup(any(SignupRequest.class));

        MvcResult mockMvc1 = this.mockMvc.perform(post("/signup")
                        .content(new ObjectMapper().writeValueAsString(signupRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        verify(userService, times(1)).signup(any(SignupRequest.class));

        assertEquals(mockMvc1.getResponse().getContentType(), "application/json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/signup");
    }

    @Test
    public void shouldntSignupInternalServerError() throws Exception {
        SignupRequest signupRequest =
                new SignupRequest("toto123", "toto", "tutu", "toto@gmail.com", "Aa!1aaaaaaaa");

        doThrow(UserServiceException.class).when(userService).signup(any(SignupRequest.class));

        MvcResult mockMvc1 = this.mockMvc.perform(post("/signup")
                        .content(new ObjectMapper().writeValueAsString(signupRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError()).andReturn();

        verify(userService, times(1)).signup(any(SignupRequest.class));

        assertEquals(mockMvc1.getResponse().getContentType(), "application/json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/signup");
    }

    @Test
    public void shouldntSignupIllegalArgumentException() throws Exception {
        SignupRequest signupRequest =
                new SignupRequest("toto123", "toto", "tutu", "toto@gmail.com", "Aa!1aaaaaaaa");

        doThrow(IllegalArgumentException.class).when(userService).signup(any(SignupRequest.class));

        MvcResult mockMvc1 = this.mockMvc.perform(post("/signup")
                        .content(new ObjectMapper().writeValueAsString(signupRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest()).andReturn();

        verify(userService, times(1)).signup(any(SignupRequest.class));

        assertEquals(mockMvc1.getResponse().getContentType(), "application/json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/signup");
    }
}

package com.todo.back.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.back.model.UserProfile;
import com.todo.back.payload.request.LoginRequest;
import com.todo.back.payload.request.SignupRequest;
import com.todo.back.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UserServiceTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PasswordEncoder encoder;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private EmailService emailService;

    @Test
    public void shouldLogin() throws Exception {
        UserProfile user = new UserProfile("Srizza93", "toto", "tutu", "toto@gmail.com", "Aa1!aaaaaaaa");
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(user.getUsername());
        loginRequest.setPassword(user.getPassword());

        when(userRepository.findByUsername(loginRequest.getUsername())).thenReturn(Optional.of(user));
        when(encoder.matches(any(CharSequence.class), anyString())).thenReturn(true);

        MvcResult mockMvc1 = this.mockMvc.perform(post("/login")
                        .content(new ObjectMapper().writeValueAsString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        assertEquals(mockMvc1.getResponse().getContentType(), "application/json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/login");

        String responseBody = mockMvc1.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(responseBody);
        JsonNode body = responseJson.path("body");
        String tokenType = body.path("tokenType").asText();
        String accessToken = body.path("accessToken").asText();

        assertEquals(tokenType, "Bearer");
        Assertions.assertFalse(accessToken.isEmpty());

        verify(userRepository, times(2)).findByUsername(loginRequest.getUsername());
        verify(encoder, times(2)).matches(any(CharSequence.class), anyString());
    }

    @Test
    public void shouldntLogin() throws Exception {
        UserProfile user = new UserProfile("Srizza93", "toto", "tutu", "toto@gmail.com", "Aa1!aaaaaaaa");
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(user.getUsername());
        loginRequest.setPassword(user.getPassword());

        MvcResult mockMvc1 = this.mockMvc.perform(post("/login")
                        .content(new ObjectMapper().writeValueAsString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError()).andReturn();

        assertEquals(mockMvc1.getResponse().getContentType(), "application/json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/login");
    }

    @Test
    public void shouldSignup() throws Exception {
        SignupRequest signupRequest =
                new SignupRequest("toto123", "toto", "tutu", "toto@gmail.com", "Aa!1aaaaaaaa");

        when(userRepository.save(any(UserProfile.class))).thenReturn(new UserProfile("toto123", "toto", "tutu", "toto@gmail.com", "Aa!1aaaaaaaa"));
        doNothing().when(emailService).sendmail(anyString());

        MvcResult mockMvc1 = this.mockMvc.perform(post("/signup")
                        .content(new ObjectMapper().writeValueAsString(signupRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        assertEquals(mockMvc1.getResponse().getContentType(), "application/json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/signup");

        verify(userRepository, times(1)).save(any(UserProfile.class));
        verify(emailService, times(1)).sendmail(anyString());
    }

    @Test
    public void shouldntSignupInvalidUsername() throws Exception {
        SignupRequest signupRequest =
                new SignupRequest("!", "toto", "tutu", "toto@gmail.com", "Aa!1aaaaaaaa");

        MvcResult mockMvc1 = this.mockMvc.perform(post("/signup")
                        .content(new ObjectMapper().writeValueAsString(signupRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isBadRequest()).andReturn();

        String responseBody = mockMvc1.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(responseBody);
        String errorMessage = responseJson.path("message").asText();

        assertEquals(mockMvc1.getResponse().getContentType(), "application/json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/signup");
        assertEquals(errorMessage, "The username format is not valid");

        verify(userRepository, never()).save(any(UserProfile.class));
    }

    @Test
    public void shouldntSignupInvalidName() throws Exception {
        SignupRequest signupRequest =
                new SignupRequest("toto123", "!", "tutu", "toto@gmail.com", "Aa!1aaaaaaaa");

        MvcResult mockMvc1 = this.mockMvc.perform(post("/signup")
                        .content(new ObjectMapper().writeValueAsString(signupRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isBadRequest()).andReturn();

        String responseBody = mockMvc1.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(responseBody);
        String errorMessage = responseJson.path("message").asText();

        assertEquals(mockMvc1.getResponse().getContentType(), "application/json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/signup");
        assertEquals(errorMessage, "The name format is not valid");

        verify(userRepository, never()).save(any(UserProfile.class));
    }

    @Test
    public void shouldntSignupInvalidSurname() throws Exception {
        SignupRequest signupRequest =
                new SignupRequest("toto123", "toto", "!", "toto@gmail.com", "Aa!1aaaaaaaa");

        MvcResult mockMvc1 = this.mockMvc.perform(post("/signup")
                        .content(new ObjectMapper().writeValueAsString(signupRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isBadRequest()).andReturn();

        String responseBody = mockMvc1.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(responseBody);
        String errorMessage = responseJson.path("message").asText();

        assertEquals(mockMvc1.getResponse().getContentType(), "application/json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/signup");
        assertEquals(errorMessage, "The surname format is not valid");

        verify(userRepository, never()).save(any(UserProfile.class));
    }

    @Test
    public void shouldntSignupInvalidEmail() throws Exception {
        SignupRequest signupRequest =
                new SignupRequest("toto123", "toto", "tutu", "totogmail.com", "Aa!1aaaaaaaa");

        MvcResult mockMvc1 = this.mockMvc.perform(post("/signup")
                        .content(new ObjectMapper().writeValueAsString(signupRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isBadRequest()).andReturn();

        String responseBody = mockMvc1.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(responseBody);
        String errorMessage = responseJson.path("message").asText();

        assertEquals(mockMvc1.getResponse().getContentType(), "application/json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/signup");
        assertEquals(errorMessage, "The email format is not valid");

        verify(userRepository, never()).save(any(UserProfile.class));
    }

    @Test
    public void shouldntSignupInvalidPassword() throws Exception {
        SignupRequest signupRequest =
                new SignupRequest("toto123", "toto", "tutu", "toto@gmail.com", "password");

        MvcResult mockMvc1 = this.mockMvc.perform(post("/signup")
                        .content(new ObjectMapper().writeValueAsString(signupRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isBadRequest()).andReturn();

        String responseBody = mockMvc1.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(responseBody);
        String errorMessage = responseJson.path("message").asText();

        assertEquals(mockMvc1.getResponse().getContentType(), "application/json");
        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/signup");
        assertEquals(errorMessage, "The password format is not valid");

        verify(userRepository, never()).save(any(UserProfile.class));
    }
}

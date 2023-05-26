package com.todo.back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.back.model.UserProfile;
import com.todo.back.payload.request.LoginRequest;
import com.todo.back.payload.request.SignupRequest;
import com.todo.back.repository.UserRepository;
import com.todo.back.services.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.*;

import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

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

        this.mockMvc.perform(post("/login")
                        .content(new ObjectMapper().writeValueAsString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldSignup() throws Exception {
        SignupRequest signupRequest =
                new SignupRequest("toto123", "toto", "tutu", "toto@gmail.com", "Aa!1aaaaaaaa");

        when(userRepository.save(any(UserProfile.class))).thenReturn(new UserProfile("toto123", "toto", "tutu", "toto@gmail.com", "Aa!1aaaaaaaa"));
        doNothing().when(emailService).sendmail(anyString());

        this.mockMvc.perform(post("/signup")
                .content(new ObjectMapper().writeValueAsString(signupRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());

        verify(userRepository, times(1)).save(any(UserProfile.class));
        verify(emailService, times(1)).sendmail(anyString());
    }

    @Test
    public void shouldntSignup() throws Exception {
        SignupRequest signupRequest =
                new SignupRequest("!", "!", "!", "!", "!");

        this.mockMvc.perform(post("/signup")
                        .content(new ObjectMapper().writeValueAsString(signupRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isBadRequest());

        verify(userRepository, never()).save(any(UserProfile.class));
    }
}

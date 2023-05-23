package com.todo.back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.back.model.UserProfile;
import com.todo.back.payload.request.SignupRequest;
import com.todo.back.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void shouldSignup() throws Exception {
        SignupRequest signupRequest =
                new SignupRequest("toto123", "toto", "tutu", "toto@gmail.com", "Aa!1aaaaaaaa");

        when(userRepository.save(any(UserProfile.class))).thenReturn(new UserProfile("toto123", "toto", "tutu", "toto@gmail.com", "Aa!1aaaaaaaa"));

        this.mockMvc.perform(post("/signup")
                .content(new ObjectMapper().writeValueAsString(signupRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());

        verify(userRepository, times(1)).save(any(UserProfile.class));
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

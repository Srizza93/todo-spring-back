package com.todo.back.services;

import com.todo.back.model.UserProfile;
import com.todo.back.payload.request.LoginRequest;
import com.todo.back.payload.response.JwtResponse;
import com.todo.back.repository.UserRepository;
import com.todo.back.security.jwt.JwtUtils;
import com.todo.back.security.services.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @MockBean
    Authentication authentication;

    @Mock
    JwtUtils jwtUtils;

    @MockBean
    private EmailService emailService;

    UserProfile user1 = new UserProfile("toto93", "toto", "tutu", "toto@gmail.com", "A!1aaaaaaaaa");

    UserProfile user2 = new UserProfile("!", "!", "!", "!", "!");

    List<UserProfile> usersList = Arrays.asList(user1, user2);

    List<GrantedAuthority> rolesList = new ArrayList<>(Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));

    private final UserRepository userRepository = mock(UserRepository.class);

    @InjectMocks
    private UserService userService;

    LoginRequest loginRequest = new LoginRequest();

    private final String mockId = "123";

    public UserServiceTest() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllUsers() {
        when(userRepository.findAll()).thenReturn(usersList);

        CollectionModel<EntityModel<UserProfile>> users = userService.users();

        verify(userRepository, times(1)).findAll();

        assertEquals(usersList.size(), users.getContent().size());
    }

    @Test
    public void shouldntGetAllUsersUserServiceException() {
        doThrow(UserServiceException.class).when(userRepository).findAll();

        assertThrows(UserServiceException.class, () -> userService.users());
    }

    @Test
    public void shouldLogin() {
        List<GrantedAuthority> authoritiesList = new ArrayList<>();
        authoritiesList.add(new SimpleGrantedAuthority("ROLE_USER"));
        authoritiesList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        loginRequest.setUsername(user1.getUsername());
        loginRequest.setPassword(user1.getPassword());
        UserDetailsImpl userDetails = new UserDetailsImpl(user1.getId(), user1.getUsername(), user1.getEmail(), user1.getPassword(), authoritiesList);

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user1));
        when(encoder.matches(anyString(), eq(user1.getPassword()))).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        ResponseEntity<JwtResponse> response = userService.login(loginRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(userRepository, times(1)).findByUsername(anyString());
        verify(encoder, times(1)).matches(anyString(), eq(user1.getPassword()));
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(authentication, times(1)).getPrincipal();;
    }

//    @Test
//    public void shouldntLogin() throws Exception {
//        UserProfile user = new UserProfile("Srizza93", "toto", "tutu", "toto@gmail.com", "Aa1!aaaaaaaa");
//        LoginRequest loginRequest = new LoginRequest();
//        loginRequest.setUsername(user.getUsername());
//        loginRequest.setPassword(user.getPassword());
//
//        MvcResult mockMvc1 = this.mockMvc.perform(post("/login")
//                        .content(new ObjectMapper().writeValueAsString(loginRequest))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isInternalServerError()).andReturn();
//
//        assertEquals(mockMvc1.getResponse().getContentType(), "application/json");
//        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
//        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/login");
//    }

//    @Test
//    public void shouldSignup() throws Exception {
//        SignupRequest signupRequest =
//                new SignupRequest("toto123", "toto", "tutu", "toto@gmail.com", "Aa!1aaaaaaaa");
//
//        when(userRepository.save(any(UserProfile.class))).thenReturn(new UserProfile("toto123", "toto", "tutu", "toto@gmail.com", "Aa!1aaaaaaaa"));
//        doNothing().when(emailService).sendmail(anyString());
//
//        MvcResult mockMvc1 = this.mockMvc.perform(post("/signup")
//                        .content(new ObjectMapper().writeValueAsString(signupRequest))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print()).andExpect(status().isOk()).andReturn();
//
//        assertEquals(mockMvc1.getResponse().getContentType(), "application/json");
//        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
//        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/signup");
//
//        verify(userRepository, times(1)).save(any(UserProfile.class));
//        verify(emailService, times(1)).sendmail(anyString());
//    }

//    @Test
//    public void shouldntSignupInvalidUsername() throws Exception {
//        SignupRequest signupRequest =
//                new SignupRequest("!", "toto", "tutu", "toto@gmail.com", "Aa!1aaaaaaaa");
//
//        MvcResult mockMvc1 = this.mockMvc.perform(post("/signup")
//                        .content(new ObjectMapper().writeValueAsString(signupRequest))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print()).andExpect(status().isBadRequest()).andReturn();
//
//        String responseBody = mockMvc1.getResponse().getContentAsString();
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode responseJson = objectMapper.readTree(responseBody);
//        String errorMessage = responseJson.path("message").asText();
//
//        assertEquals(mockMvc1.getResponse().getContentType(), "application/json");
//        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
//        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/signup");
//        assertEquals(errorMessage, "The username format is not valid");
//
//        verify(userRepository, never()).save(any(UserProfile.class));
//    }

//    @Test
//    public void shouldntSignupInvalidName() throws Exception {
//        SignupRequest signupRequest =
//                new SignupRequest("toto123", "!", "tutu", "toto@gmail.com", "Aa!1aaaaaaaa");
//
//        MvcResult mockMvc1 = this.mockMvc.perform(post("/signup")
//                        .content(new ObjectMapper().writeValueAsString(signupRequest))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print()).andExpect(status().isBadRequest()).andReturn();
//
//        String responseBody = mockMvc1.getResponse().getContentAsString();
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode responseJson = objectMapper.readTree(responseBody);
//        String errorMessage = responseJson.path("message").asText();
//
//        assertEquals(mockMvc1.getResponse().getContentType(), "application/json");
//        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
//        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/signup");
//        assertEquals(errorMessage, "The name format is not valid");
//
//        verify(userRepository, never()).save(any(UserProfile.class));
//    }

//    @Test
//    public void shouldntSignupInvalidSurname() throws Exception {
//        SignupRequest signupRequest =
//                new SignupRequest("toto123", "toto", "!", "toto@gmail.com", "Aa!1aaaaaaaa");
//
//        MvcResult mockMvc1 = this.mockMvc.perform(post("/signup")
//                        .content(new ObjectMapper().writeValueAsString(signupRequest))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print()).andExpect(status().isBadRequest()).andReturn();
//
//        String responseBody = mockMvc1.getResponse().getContentAsString();
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode responseJson = objectMapper.readTree(responseBody);
//        String errorMessage = responseJson.path("message").asText();
//
//        assertEquals(mockMvc1.getResponse().getContentType(), "application/json");
//        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
//        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/signup");
//        assertEquals(errorMessage, "The surname format is not valid");
//
//        verify(userRepository, never()).save(any(UserProfile.class));
//    }

//    @Test
//    public void shouldntSignupInvalidEmail() throws Exception {
//        SignupRequest signupRequest =
//                new SignupRequest("toto123", "toto", "tutu", "totogmail.com", "Aa!1aaaaaaaa");
//
//        MvcResult mockMvc1 = this.mockMvc.perform(post("/signup")
//                        .content(new ObjectMapper().writeValueAsString(signupRequest))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print()).andExpect(status().isBadRequest()).andReturn();
//
//        String responseBody = mockMvc1.getResponse().getContentAsString();
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode responseJson = objectMapper.readTree(responseBody);
//        String errorMessage = responseJson.path("message").asText();
//
//        assertEquals(mockMvc1.getResponse().getContentType(), "application/json");
//        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
//        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/signup");
//        assertEquals(errorMessage, "The email format is not valid");
//
//        verify(userRepository, never()).save(any(UserProfile.class));
//    }

//    @Test
//    public void shouldntSignupInvalidPassword() throws Exception {
//        SignupRequest signupRequest =
//                new SignupRequest("toto123", "toto", "tutu", "toto@gmail.com", "password");
//
//        MvcResult mockMvc1 = this.mockMvc.perform(post("/signup")
//                        .content(new ObjectMapper().writeValueAsString(signupRequest))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print()).andExpect(status().isBadRequest()).andReturn();
//
//        String responseBody = mockMvc1.getResponse().getContentAsString();
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode responseJson = objectMapper.readTree(responseBody);
//        String errorMessage = responseJson.path("message").asText();
//
//        assertEquals(mockMvc1.getResponse().getContentType(), "application/json");
//        assertEquals(mockMvc1.getRequest().getServerPort(), 80);
//        assertEquals(mockMvc1.getRequest().getRequestURL().toString(), "http://localhost/signup");
//        assertEquals(errorMessage, "The password format is not valid");
//
//        verify(userRepository, never()).save(any(UserProfile.class));
//    }
}

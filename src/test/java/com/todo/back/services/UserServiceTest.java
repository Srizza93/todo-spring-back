package com.todo.back.services;

import com.todo.back.model.ERole;
import com.todo.back.model.UserProfile;
import com.todo.back.payload.request.LoginRequest;
import com.todo.back.payload.request.SignupRequest;
import com.todo.back.payload.response.JwtResponse;
import com.todo.back.repository.UserRepository;
import com.todo.back.security.jwt.JwtUtils;
import com.todo.back.security.services.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    JwtUtils jwtUtils;

    EmailService emailService = mock(EmailService.class);

    @MockBean
    Authentication authentication;

    @InjectMocks
    private UserService userService;

    UserProfile user1 = new UserProfile("toto93", "toto", "tutu", "toto@gmail.com", "A!1aaaaaaaaa");

    UserProfile user2 = new UserProfile("!", "!", "!", "!", "!");

    List<UserProfile> usersList = Arrays.asList(user1, user2);

    private final UserRepository userRepository = mock(UserRepository.class);

    LoginRequest loginRequest = new LoginRequest();

    SignupRequest signupRequest =
            new SignupRequest("toto123", "toto", "tutu", "toto@gmail.com", "Aa!1aaaaaaaa");

    private final String mockId = "123";

    private final String userRole = ERole.userRole;

    public UserServiceTest() {
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

    @Test
    public void shouldntLoginInvalidUsername() {
        loginRequest.setUsername(user1.getUsername());
        loginRequest.setPassword(user1.getPassword());

        Exception exception = assertThrows(UserServiceException.class, () -> userService.login(loginRequest));

        assertEquals("Invalid username", exception.getMessage());

        verify(userRepository, times(1)).findByUsername(anyString());
    }

    @Test
    public void shouldntLoginInvalidPassword() {
        loginRequest.setUsername(user1.getUsername());
        loginRequest.setPassword(user1.getPassword());

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user1));

        Exception exception = assertThrows(UserServiceException.class, () -> userService.login(loginRequest));

        assertEquals("Invalid password", exception.getMessage());

        verify(userRepository, times(1)).findByUsername(anyString());
        verify(encoder, times(1)).matches(anyString(), eq(user1.getPassword()));
    }

    @Test
    public void shouldSignup() {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        userService.signup(signupRequest);

        verify(userRepository, times(1)).existsByUsername(anyString());
        verify(userRepository, times(1)).existsByEmail(anyString());
    }

    @Test
    public void shouldntSignupUsedUsername() {
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.signup(signupRequest));

        assertEquals("This username has been used already", exception.getMessage());

        verify(userRepository, times(1)).existsByUsername(anyString());
    }

    @Test
    public void shouldntSignupInvalidUsernameFormat() {
        signupRequest.setUsername("!");
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.signup(signupRequest));

        assertEquals("The username format is not valid", exception.getMessage());

        verify(userRepository, times(1)).existsByUsername(anyString());
        verify(userRepository, times(1)).existsByEmail(anyString());
    }

    @Test
    public void shouldntSignupUsedEmail() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.signup(signupRequest));

        assertEquals("This email has been used already", exception.getMessage());

        verify(userRepository, times(1)).existsByEmail(anyString());
    }

    @Test
    public void shouldntSignupInvalidEmailFormat() {
        signupRequest.setEmail("!");
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.signup(signupRequest));

        assertEquals("The email format is not valid", exception.getMessage());

        verify(userRepository, times(1)).existsByUsername(anyString());
        verify(userRepository, times(1)).existsByEmail(anyString());
    }

    @Test
    public void shouldntSignupInvalidNameFormat() {
        signupRequest.setName("!");
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.signup(signupRequest));

        assertEquals("The name format is not valid", exception.getMessage());

        verify(userRepository, times(1)).existsByUsername(anyString());
        verify(userRepository, times(1)).existsByEmail(anyString());
    }

    @Test
    public void shouldntSignupInvalidSurnameFormat() {
        signupRequest.setSurname("!");
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.signup(signupRequest));

        assertEquals("The surname format is not valid", exception.getMessage());

        verify(userRepository, times(1)).existsByUsername(anyString());
        verify(userRepository, times(1)).existsByEmail(anyString());
    }

    @Test
    public void shouldntSignupInvalidPasswordFormat() {
        signupRequest.setPassword("!");
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.signup(signupRequest));

        assertEquals("The password format is not valid", exception.getMessage());

        verify(userRepository, times(1)).existsByUsername(anyString());
        verify(userRepository, times(1)).existsByEmail(anyString());
    }

    @Test
    public void shouldSignupWithAdminRolePreset() {
        signupRequest.setRole(Collections.singleton("admin"));
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        userService.signup(signupRequest);

        verify(userRepository, times(1)).existsByUsername(anyString());
        verify(userRepository, times(1)).existsByEmail(anyString());
    }

    @Test
    public void shouldSignupWithModeratorRolePreset() {
        signupRequest.setRole(Collections.singleton("mod"));
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        userService.signup(signupRequest);

        verify(userRepository, times(1)).existsByUsername(anyString());
        verify(userRepository, times(1)).existsByEmail(anyString());
    }

    @Test
    public void shouldSignupWithUserRolePreset() {
        signupRequest.setRole(Collections.singleton("ROLE_USER"));
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        userService.signup(signupRequest);

        verify(userRepository, times(1)).existsByUsername(anyString());
        verify(userRepository, times(1)).existsByEmail(anyString());
    }

    @Test
    public void shouldntSendConfirmationEmail() throws Exception {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        doThrow(UserServiceException.class).when(emailService).sendmail(anyString());

        assertThrows(UserServiceException.class, () -> userService.signup(signupRequest));

        verify(userRepository, times(1)).existsByUsername(anyString());
        verify(userRepository, times(1)).existsByEmail(anyString());
        verify(emailService, times(1)).sendmail(anyString());
    }
}
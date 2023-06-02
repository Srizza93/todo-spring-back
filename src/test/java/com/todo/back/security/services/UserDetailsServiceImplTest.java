package com.todo.back.security.services;

import com.todo.back.model.UserProfile;
import com.todo.back.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserDetailsServiceImplTest {

    @InjectMocks
    UserDetailsServiceImpl userDetailsService;

    @Mock
    UserRepository userRepository;

    UserProfile userProfile = new UserProfile("toto93", "toto", "tutu", "toto@gmail.com", "A!1aaaaaaaaa");

    @Test
    public void shouldLoadUserByName() {
        when(userRepository.findByUsername("toto")).thenReturn(Optional.of(userProfile));

        userDetailsService.loadUserByUsername("toto");

        verify(userRepository, times(1)).findByUsername("toto");
    }

    @Test
    public void shouldntLoadUserByName() {
        doThrow(UsernameNotFoundException.class).when(userRepository).findByUsername("toto");

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("toto"));
    }
}

package com.todo.back.security.jwt;

import com.todo.back.security.services.UserDetailsImpl;
import com.todo.back.security.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthTokenFilterTest {

    @InjectMocks
    AuthTokenFilter authTokenFilter;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    FilterChain filterChain;

    @Mock
    JwtUtils jwtUtils;

    @Mock
    UserDetailsServiceImpl userDetailsService;

    @Mock
    UsernamePasswordAuthenticationToken authentication;

    @Mock
    SecurityContext securityContext;

    @Test
    public void shouldDoFilterInternal() throws ServletException, IOException {
        authTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    public void shouldDoFilterInternalExistingJwt() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer mock");
        when(jwtUtils.validateJwtToken(any())).thenReturn(true);

        authTokenFilter.doFilterInternal(request, response, filterChain);

        verify(request, times(1)).getHeader("Authorization");
        verify(jwtUtils, times(1)).validateJwtToken(anyString());
        verify(jwtUtils, times(1)).getUserNameFromJwtToken(anyString());
        verify(userDetailsService, times(1)).loadUserByUsername(any());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    public void shouldntDoFilterInternalException() throws ServletException, IOException {
        doThrow(IOException.class).when(filterChain).doFilter(request, response);

        assertThrows(IOException.class, () -> authTokenFilter.doFilterInternal(request, response, filterChain));
    }

    @Test
    public void shouldGetAuthenticationDetails() {
        authTokenFilter.getAuthenticationDetails(request);
    }

    @Test
    public void shouldParseJwt() {
        when(request.getHeader("Authorization")).thenReturn("Bearer mock");

        authTokenFilter.parseJwt(request);

        verify(request, times(1)).getHeader("Authorization");
    }
}

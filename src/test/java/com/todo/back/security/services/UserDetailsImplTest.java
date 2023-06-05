package com.todo.back.security.services;

import com.todo.back.model.ERole;
import com.todo.back.model.Role;
import com.todo.back.model.UserProfile;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserDetailsImplTest {

    @Mock
    Object o;

    UserProfile userProfile = new UserProfile("toto93", "toto", "tutu", "toto@gmail.com", "A!1aaaaaaaaa");

    UserDetailsImpl userDetails1 = new UserDetailsImpl("1", "toto93", "toto@gmail.com", "A!1aaaaaaaaa",  Collections.emptyList());

    @Test
    public void shouldBuildUserDetails() {
        Role role = new Role(ERole.ROLE_USER);
        role.setName(ERole.ROLE_USER);

        userProfile.setRoles(Set.of(role));

        UserDetailsImpl userDetails2 = UserDetailsImpl.build(userProfile);

        assertEquals(userDetails2.getUsername(), userProfile.getUsername());
        assertEquals(userDetails2.getId(), userProfile.getId());
        assertEquals(userDetails2.getPassword(), userProfile.getPassword());
        assertEquals(userDetails2.getEmail(), userProfile.getEmail());

        Collection<? extends GrantedAuthority> authorities = userDetails2.getAuthorities();

        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
        assertTrue(userDetails2.isAccountNonExpired());
        assertTrue(userDetails2.isAccountNonLocked());
        assertTrue(userDetails2.isEnabled());
        assertTrue(userDetails2.isCredentialsNonExpired());
    }

    @Test
    public void shouldEqualsReturnUser() {
        UserDetailsImpl userDetails2 = new UserDetailsImpl("1", "toto93", "toto@gmail.com", "A!1aaaaaaaaa",  Collections.emptyList());
        Boolean equals = userDetails1.equals(userDetails2);

        assertTrue(equals);
    }

    @Test
    public void shouldEqualsReturnTrue() {
        Boolean equals = userDetails1.equals(userDetails1);

        assertTrue(equals);
    }

    @Test
    public void shouldEqualsReturnFalse() {
        Boolean equals = userDetails1.equals(o);

        assertFalse(equals);
    }

}

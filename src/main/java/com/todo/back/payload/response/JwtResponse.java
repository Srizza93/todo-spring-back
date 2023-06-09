package com.todo.back.payload.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Set;

public class JwtResponse {

    private String token;

    @NotNull
    private String type = "Bearer";

    @Id
    private Long id;

    @Size(min=3, max=30, message = "Username size is incorrect")
    @NotNull
    private String username;

    @Email
    @Size(min=5, max=100, message = "Email size is incorrect")
    @NotNull
    private String email;

    private Set<String> roles;

    public JwtResponse(String accessToken, Long id, String username, String email, Set<String>  roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<String> getRoles() {
        return roles;
    }
}

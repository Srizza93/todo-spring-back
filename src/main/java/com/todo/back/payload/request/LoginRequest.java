package com.todo.back.payload.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class LoginRequest {

    @Size(min=3, max=30, message = "Username size is incorrect")
    @NotNull
    private String username;

    @Size(min=8, max=256, message = "Password size is incorrect")
    @NotNull
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

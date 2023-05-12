package com.todo.back.payload.request;

import java.util.Set;

public class SignupRequest {

    private String username;

    private String name;

    private String surname;

    private String email;

    private Set<String> roles;

    private String password;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return this.roles;
    }
    public void setRole(Set<String> roles) {
        this.roles = roles;
    }
}

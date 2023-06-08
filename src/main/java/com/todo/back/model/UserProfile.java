package com.todo.back.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_profile")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @Size(min=3, max=30, message = "Username size is incorrect")
    @NotNull
    private String username;

    @Size(min=1, max=30, message = "Name size is incorrect")
    @NotNull
    private String name;

    @Size(min=2, max=30, message = "Surname size is incorrect")
    @NotNull
    private String surname;

    @Email
    @Size(min=5, max=100, message = "Email size is incorrect")
    @NotNull
    private String email;

    @Size(min=8, max=256, message = "Password size is incorrect")
    @NotNull
    private String password;

//    @DBRef
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition="JSON")
    private Set<Role> roles = new HashSet<>();

    public UserProfile(String username, String name, String surname, String email, String password) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}

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
@Table(name = "user_profiles")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Size(min=3, max=30, message = "Username size is incorrect")
    @NotNull
    @Column(name = "username")
    private String username;

    @Size(min=1, max=30, message = "Name size is incorrect")
    @NotNull
    @Column(name = "name")
    private String name;

    @Size(min=2, max=30, message = "Surname size is incorrect")
    @NotNull
    @Column(name = "surname")
    private String surname;

    @Email
    @Size(min=5, max=100, message = "Email size is incorrect")
    @NotNull
    @Column(name = "email")
    private String email;

    @Size(min=8, max=256, message = "Password size is incorrect")
    @NotNull
    @Column(name = "password")
    private String password;

    @NotNull
//    @ElementCollection
    @Column(name = "roles")
    private Set<String> roles = new HashSet<>();

    public UserProfile() {}

    public UserProfile(String username, String name, String surname, String email, String password) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "Surname: " +  this.surname + " Name: " + this.name + " Email: " + this.email + " Password: " + this.password + " Roles: " +
                this.roles + " Id: " + this.id + " Username: " + this.username;
    }
}

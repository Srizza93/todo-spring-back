package com.todo.back.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("UserProfile")
public class UserProfile {

    @Id
    private String id;
    private String name;
    private String surname;
    private String email;
    private String password;

    public UserProfile(String id, String name, String surname, String email, String password) {
        super();
        this.id = id;
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

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

}

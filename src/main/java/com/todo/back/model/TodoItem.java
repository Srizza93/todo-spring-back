package com.todo.back.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jdk.jfr.BooleanFlag;
import jdk.jfr.Timestamp;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "todo_items")
public class TodoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @NotNull
    private String userId;

    @Size(max=100, message = "Content is too long")
    private String content;

    @Timestamp
    private Date due;

    @NotNull
    @Timestamp
    private Date created;

    @NotNull
    @BooleanFlag
    private  Boolean done;

    public TodoItem(String id, String userId, String content, Date due, Date created) {
        super();
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.due = due;
        this.created = created;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDue() {
        return due;
    }

    public void setDue(Date due) {
        this.due = due;
    }

    public Date getCreated() { return created; }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Boolean getDone() { return done; }

    public void setDone(Boolean done) {
        this.done = done;
    }

}


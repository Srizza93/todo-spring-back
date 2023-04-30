package com.todo.back.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("TodoItem")
public class TodoItem {

    @Id
    private String id;

    private String userId;
    private String content;
    private String due;
    private Date created;

    private  Boolean done;

    public TodoItem(String id, String userId, String content, String due, Date created) {
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

    public String getDue() {
        return due;
    }

    public void setDue(String due) {
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


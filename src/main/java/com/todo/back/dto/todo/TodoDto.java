package com.todo.back.dto.todo;

import java.util.Date;

public class TodoDto {

    private String id;
    private String userId;
    private String content;
    private Date due;
    private Date created;

    private  Boolean done;

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

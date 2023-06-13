package com.todo.back.dto;

import java.util.Date;

public class TodoDto {

    private Long id;
    private Long userId;
    private String content;
    private Date due;
    private Date created;

    private  Boolean done;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
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

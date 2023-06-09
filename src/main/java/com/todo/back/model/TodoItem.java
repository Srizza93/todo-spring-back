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
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "user_id")
    private Long userId;

    @Size(max=100, message = "Content is too long")
    @Column(name = "content")
    private String content;

    @Timestamp
    @Column(name = "due")
    private Date due;

    @NotNull
    @Timestamp
    @Column(name = "created")
    private Date created;

    @NotNull
    @BooleanFlag
    @Column(name = "done")
    private  Boolean done;

    public TodoItem() {}

    public TodoItem(Long id, Long userId, String content, Date due, Date created) {
        super();
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.due = due;
        this.created = created;
    }

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


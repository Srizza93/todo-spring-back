package com.todo.back.controller;

import com.todo.back.model.TodoItem;
import com.todo.back.services.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TodoItemController {

    private final TodoService todoService;

    TodoItemController(TodoService todoService) {
        this.todoService = todoService;
    }

    // Aggregate root

    // tag::get-aggregate-root[]
    @GetMapping("/todos")
    public ResponseEntity<?> all() {

        try {
            return ResponseEntity.ok(todoService.todos());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    // end::get-aggregate-root[]

    // tag::get-done-user[]
    @GetMapping("/todos/TODAY/{userId}")
    ResponseEntity<?> allToday(@PathVariable String userId) {
        try {
            return ResponseEntity.ok(todoService.today(userId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    // end::get-done-user[]

    // tag::get-inbox-user[]
    @GetMapping("/todos/INBOX/{userId}")
    ResponseEntity<?> allInbox(@PathVariable String userId) {
        try {
            return ResponseEntity.ok(todoService.inbox(userId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    // end::get-inbox-user[]

    // tag::get-done-user[]
    @GetMapping("/todos/DONE/{userId}")
    ResponseEntity<?> allDone(@PathVariable String userId) {
        try {
            return ResponseEntity.ok(todoService.done(userId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    // end::get-done-user[]

    // tag::post-new-todo[]
    @PostMapping("/todos")
    ResponseEntity<?> newTodo(@RequestBody TodoItem newTodo) {
        try {
            return ResponseEntity.ok(todoService.addTodo(newTodo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    // end::post-new-todo[]

    // tag::check-todo[]
    @PutMapping("/todos")
    ResponseEntity<?> replaceTodo(@RequestBody TodoItem newTodo) {
        try {
            return ResponseEntity.ok(todoService.editTodoStatus(newTodo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    // end::check-todo[]

    // tag::delete-todo[]
    @DeleteMapping("/todos/{id}")
    ResponseEntity<?> deleteEmployee(@PathVariable String id) {
        try {
            return ResponseEntity.ok(todoService.deleteTodo(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    // end::delete-todo[]
}

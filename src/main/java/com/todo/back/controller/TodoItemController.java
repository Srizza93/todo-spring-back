package com.todo.back.controller;

import com.todo.back.dto.TodoDto;
import com.todo.back.services.TodoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.todo.back.exception.ControllerExceptionHandler.handleInternalServerException;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class TodoItemController {

    private final TodoService todoService;

    TodoItemController(TodoService todoService) {
        this.todoService = todoService;
    }

    // Aggregate root

    // tag::get-aggregate-root[]
    @GetMapping("/todos")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> getAllTodos() {
        try {
            return ResponseEntity.ok(todoService.todos());
        } catch (Exception e) {
            return handleInternalServerException(e);
        }
    }
    // end::get-aggregate-root[]

    // tag::get-done-user[]
    @GetMapping("/todos/TODAY/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    ResponseEntity<?> allToday(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(todoService.today(userId));
        } catch (Exception e) {
            return handleInternalServerException(e);
        }
    }
    // end::get-done-user[]

    // tag::get-inbox-user[]
    @GetMapping("/todos/INBOX/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    ResponseEntity<?> allInbox(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(todoService.inbox(userId));
        } catch (Exception e) {
            return handleInternalServerException(e);
        }
    }
    // end::get-inbox-user[]

    // tag::get-done-user[]
    @GetMapping("/todos/DONE/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    ResponseEntity<?> allDone(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(todoService.done(userId));
        } catch (Exception e) {
            return handleInternalServerException(e);
        }
    }
    // end::get-done-user[]

    // tag::post-new-todo[]
    @PostMapping("/todos")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    ResponseEntity<?> newTodo(@RequestBody TodoDto todoDto) {
        try {
            return ResponseEntity.ok(todoService.addTodo(todoDto));
        } catch (Exception e) {
            return handleInternalServerException(e);
        }
    }
    // end::post-new-todo[]

    // tag::check-todo[]
    @PutMapping("/todos")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    ResponseEntity<?> replaceTodo(@RequestBody TodoDto todoDto) {
        try {
            return ResponseEntity.ok(todoService.editTodoStatus(todoDto));
        } catch (Exception e) {
            return handleInternalServerException(e);
        }
    }
    // end::check-todo[]

    // tag::delete-todo[]
    @DeleteMapping("/todos/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    ResponseEntity<?> deleteTodo(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(todoService.deleteTodo(id));
        } catch (Exception e) {
            return handleInternalServerException(e);
        }
    }
    // end::delete-todo[]
}

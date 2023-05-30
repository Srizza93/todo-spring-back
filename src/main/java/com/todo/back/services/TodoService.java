package com.todo.back.services;

import com.todo.back.controller.TodoItemController;
import com.todo.back.dto.TodoDto;
import com.todo.back.model.TodoItem;
import com.todo.back.repository.ItemRepository;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class TodoService {

    private final ItemRepository repository;

    public TodoService(ItemRepository repository) {
        this.repository = repository;
    }

    public CollectionModel<EntityModel<TodoItem>> todos() throws UserServiceException {

        try {
            List<EntityModel<TodoItem>> todos = repository.findAll().stream()
                    .map(todo -> EntityModel.of(todo,
                            linkTo(methodOn(TodoItemController.class).getAllTodos()).withRel("TodoItem")))
                    .collect(Collectors.toList());

            return CollectionModel.of(todos, linkTo(methodOn(TodoItemController.class).getAllTodos()).withSelfRel());
        } catch (Exception e) {
            throw new UserServiceException("Failed to fetch todos", e);
        }
    }

    public CollectionModel<EntityModel<TodoItem>> today(String userId) throws UserServiceException {
        try {
            LocalDate localDate = LocalDate.now();
            LocalDateTime today = localDate.atTime(LocalTime.MAX);

            List<EntityModel<TodoItem>> todos = repository.findByUserIdAndDoneAndDueLessThanEqual(userId, false, today).stream()
                    .map(todo -> EntityModel.of(todo,
                            linkTo(methodOn(TodoItemController.class).getAllTodos()).withRel("TodoItem")))
                    .collect(Collectors.toList());

            return CollectionModel.of(todos, linkTo(methodOn(TodoItemController.class).getAllTodos()).withSelfRel());
        } catch (Exception e) {
            throw new UserServiceException("Failed to fetch today's todos", e);
        }
    }

    public CollectionModel<EntityModel<TodoItem>> inbox(String userId) throws UserServiceException {

        try {
            List<EntityModel<TodoItem>> todos = repository.findByUserIdAndDone(userId, false).stream()
                    .map(todo -> EntityModel.of(todo,
                            linkTo(methodOn(TodoItemController.class).getAllTodos()).withRel("TodoItem")))
                    .collect(Collectors.toList());

            return CollectionModel.of(todos, linkTo(methodOn(TodoItemController.class).getAllTodos()).withSelfRel());
        } catch (Exception e) {
            throw new UserServiceException("Failed to fetch inbox's todos", e);
        }
    }

    public CollectionModel<EntityModel<TodoItem>> done(String userId) throws UserServiceException {

        try {
            List<EntityModel<TodoItem>> todos = repository.findByUserIdAndDone(userId, true).stream()
                    .map(todo -> EntityModel.of(todo,
                            linkTo(methodOn(TodoItemController.class).getAllTodos()).withRel("TodoItem")))
                    .collect(Collectors.toList());

            return CollectionModel.of(todos, linkTo(methodOn(TodoItemController.class).getAllTodos()).withSelfRel());
        } catch (Exception e) {
            throw new UserServiceException("Failed to fetch done todos", e);
        }
    }

    public TodoItem addTodo(TodoDto todoDto) throws UserServiceException {
        try {
            TodoItem todo = getTodoFromDto(todoDto);

            if (todo.getContent().length() > 100) {
                throw new IllegalArgumentException("The content is too long");
            }

            return repository.save(todo);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new UserServiceException("Failed to add a new todo");
        }
    }

    public TodoItem editTodoStatus(TodoDto todoDto) throws UserServiceException {

        try {
            return repository.findById(todoDto.getId()) //
                    .map(todo -> {
                        todo.setDone(todoDto.getDone());
                        return repository.save(todo);
                    }) //
                    .orElseGet(() -> {
                        todoDto.setDone(todoDto.getDone());

                        TodoItem todo = getTodoFromDto(todoDto);

                        return repository.save(todo);
                    });
        } catch (Exception e) {
            throw new UserServiceException("Failed to edit the todo status");
        }
    }

    public ResponseEntity<?> deleteTodo(String id) throws UserServiceException {

        try {
            repository.deleteById(id);

            return ResponseEntity.ok("Successfully deleted the todo");
        } catch (Exception e) {
            throw new UserServiceException("Failed to delete the todo", e);
        }
    }

    public TodoItem getTodoFromDto(TodoDto todoDto) {
        String userId = todoDto.getUserId();
        String content = todoDto.getContent();
        Date due = todoDto.getDue();
        Date created = todoDto.getCreated();

        return new TodoItem(null, userId, content, due, created);
    }
}

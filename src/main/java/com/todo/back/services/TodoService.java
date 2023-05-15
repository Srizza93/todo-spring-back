package com.todo.back.services;

import com.todo.back.controller.TodoItemController;
import com.todo.back.dto.TodoDto;
import com.todo.back.model.TodoItem;
import com.todo.back.repository.ItemRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class TodoService {

    private final ItemRepository repository;

    private final Validator validator;

    public TodoService(ItemRepository repository) {
        this.repository = repository;
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public CollectionModel<EntityModel<TodoItem>> todos() {

        List<EntityModel<TodoItem>> todos = repository.findAll().stream()
                .map(todo -> EntityModel.of(todo,
                        linkTo(methodOn(TodoItemController.class).all()).withRel("TodoItem")))
                .collect(Collectors.toList());

        return CollectionModel.of(todos, linkTo(methodOn(TodoItemController.class).all()).withSelfRel());
    }

    public CollectionModel<EntityModel<TodoItem>> today(String userId) {
        LocalDate localDate = LocalDate.now();
        LocalDateTime today = localDate.atTime(LocalTime.MAX);

        List<EntityModel<TodoItem>> todos = repository.findByUserIdAndDoneAndDueLessThanEqual(userId, false, today).stream()
                .map(todo -> EntityModel.of(todo,
                        linkTo(methodOn(TodoItemController.class).all()).withRel("TodoItem")))
                .collect(Collectors.toList());

        return CollectionModel.of(todos, linkTo(methodOn(TodoItemController.class).all()).withSelfRel());
    }

    public CollectionModel<EntityModel<TodoItem>> inbox(String userId) {

        List<EntityModel<TodoItem>> todos = repository.findByUserIdAndDone(userId, false).stream()
                .map(todo -> EntityModel.of(todo,
                        linkTo(methodOn(TodoItemController.class).all()).withRel("TodoItem")))
                .collect(Collectors.toList());

        return CollectionModel.of(todos, linkTo(methodOn(TodoItemController.class).all()).withSelfRel());
    }

    public CollectionModel<EntityModel<TodoItem>> done(String userId) {

        List<EntityModel<TodoItem>> todos = repository.findByUserIdAndDone(userId, true).stream()
                .map(todo -> EntityModel.of(todo,
                        linkTo(methodOn(TodoItemController.class).all()).withRel("TodoItem")))
                .collect(Collectors.toList());

        return CollectionModel.of(todos, linkTo(methodOn(TodoItemController.class).all()).withSelfRel());
    }

    public TodoItem addTodo(TodoDto todoDto) {

        TodoItem todo = getTodoFromDto(todoDto);
        Set<ConstraintViolation<String>> violations = validator.validate(todo.getContent());

        if (todo.getContent().length() > 100) {
            throw new ConstraintViolationException("The content is too long", violations);
        }

        todo.setDone(false);

        return repository.save(todo);
    }

    public TodoItem editTodoStatus(TodoDto todoDto) {

        if (todoDto == null) {
            throw new IllegalArgumentException("Todo is invalid, can't edit the status");
        }

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
    }

    public boolean deleteTodo(String id) {

        if (id == null) {
            throw new IllegalArgumentException("Id is missing, can't delete");
        }

        repository.deleteById(id);

        return true;
    }

    public TodoItem getTodoFromDto(TodoDto todoDto) {
        String userId = todoDto.getUserId();
        String content = todoDto.getContent();
        Date due = todoDto.getDue();
        Date created = todoDto.getCreated();

        return new TodoItem(null, userId, content, due, created);
    }
}

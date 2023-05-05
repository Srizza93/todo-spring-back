package com.todo.back.services;

import com.todo.back.controller.TodoItemController;
import com.todo.back.model.TodoItem;
import com.todo.back.repository.todo.ItemRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    public TodoItem addTodo(TodoItem newTodo) {

        return repository.save(newTodo);
    }

    public TodoItem editTodoStatus(TodoItem newTodo) {

        return repository.findById(newTodo.getId()) //
                .map(todo -> {
                    todo.setDone(newTodo.getDone());
                    return repository.save(todo);
                }) //
                .orElseGet(() -> {
                    newTodo.setDone(newTodo.getDone());
                    return repository.save(newTodo);
                });
    }

    public boolean deleteTodo(String id) {

        repository.deleteById(id);

        return true;
    }
}

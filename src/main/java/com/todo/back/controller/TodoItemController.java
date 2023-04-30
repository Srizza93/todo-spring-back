package com.todo.back.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.todo.back.model.TodoItem;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import com.todo.back.repository.todo.ItemRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.List;

@RestController
public class TodoItemController {

    private final ItemRepository repository;

    TodoItemController(ItemRepository repository) {
        this.repository = repository;
    }

    // Aggregate root

    // tag::get-aggregate-root[]
    @GetMapping("/todos")
    CollectionModel<EntityModel<TodoItem>> all() {

        List<EntityModel<TodoItem>> employees = repository.findAll().stream()
                .map(employee -> EntityModel.of(employee,
                        linkTo(methodOn(TodoItemController.class).all()).withRel("employees")))
                .collect(Collectors.toList());

        return CollectionModel.of(employees, linkTo(methodOn(TodoItemController.class).all()).withSelfRel());
    }
    // end::get-aggregate-root[]

    // tag::get-done-user[]
    @GetMapping("/todos/TODAY/{userId}")
    CollectionModel<EntityModel<TodoItem>> allToday(@PathVariable String userId) {

        String today = LocalDate.now().parse("2023-05-01").toString();

        List<EntityModel<TodoItem>> todos = repository.findByUserToday(userId, today).stream()
                .map(todo -> EntityModel.of(todo,
                        linkTo(methodOn(TodoItemController.class).all()).withRel("TodoItem")))
                .collect(Collectors.toList());

        return CollectionModel.of(todos, linkTo(methodOn(TodoItemController.class).all()).withSelfRel());
    }
    // end::get-done-user[]

    // tag::get-inbox-user[]
    @GetMapping("/todos/INBOX/{userId}")
    CollectionModel<EntityModel<TodoItem>> allInbox(@PathVariable String userId) {

        List<EntityModel<TodoItem>> todos = repository.findByUserInbox(userId).stream()
                .map(todo -> EntityModel.of(todo,
                        linkTo(methodOn(TodoItemController.class).all()).withRel("TodoItem")))
                .collect(Collectors.toList());

        return CollectionModel.of(todos, linkTo(methodOn(TodoItemController.class).all()).withSelfRel());
    }
    // end::get-inbox-user[]

    // tag::get-done-user[]
    @GetMapping("/todos/DONE/{userId}")
    CollectionModel<EntityModel<TodoItem>> allDone(@PathVariable String userId) {

        List<EntityModel<TodoItem>> todos = repository.findByUserDone(userId).stream()
                .map(todo -> EntityModel.of(todo,
                        linkTo(methodOn(TodoItemController.class).all()).withRel("TodoItem")))
                .collect(Collectors.toList());

        return CollectionModel.of(todos, linkTo(methodOn(TodoItemController.class).all()).withSelfRel());
    }
    // end::get-done-user[]

    // tag::post-new-todo[]
    @PostMapping("/todos")
    TodoItem newTodo(@RequestBody TodoItem newTodo) {

        return repository.save(newTodo);
    }
    // end::post-new-todo[]

    // tag::check-todo[]
    @PutMapping("/todos")
    TodoItem replaceTodo(@RequestBody TodoItem newTodo) {

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
    // end::check-todo[]

    // tag::delete-todo[]
    @DeleteMapping("/todos/{id}")
    void deleteEmployee(@PathVariable String id) {
        repository.deleteById(id);
    }
    // end::delete-todo[]
}

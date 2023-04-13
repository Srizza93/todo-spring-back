package com.todo.back.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.todo.back.model.TodoItem;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import com.todo.back.repository.todo.ItemRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

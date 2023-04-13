package com.todo.back.controller;

import com.todo.back.model.TodoItem;
import com.todo.back.model.UserProfile;
import com.todo.back.repository.user.UserRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserController {

    private final UserRepository repository;

    UserController(UserRepository repository) {
        this.repository = repository;
    }

    // tag::get-aggregate-root[]
    @GetMapping("/users")
    CollectionModel<EntityModel<UserProfile>> all() {

        List<EntityModel<UserProfile>> users = repository.findAll().stream()
                .map(user -> EntityModel.of(user,
                        linkTo(methodOn(UserController.class).all()).withRel("users")))
                .collect(Collectors.toList());

        return CollectionModel.of(users, linkTo(methodOn(UserController.class).all()).withSelfRel());
    }
    // end::get-aggregate-root[]

    // tag::get-single-item[]
    @GetMapping("/users/{email}")
    EntityModel<UserProfile> one(@PathVariable String email) {

        UserProfile user = repository.findUserByEmail(email);

        return EntityModel.of(user, //
                linkTo(methodOn(UserController.class).one(email)).withSelfRel(),
                linkTo(methodOn(UserController.class).all()).withRel("users"));
    }

    // end::get-single-item[]
}

package com.todo.back.controller;

import com.todo.back.model.UserProfile;
import com.todo.back.repository.user.UserRepository;
import com.todo.back.services.UserService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserController {

    private final UserRepository repository;
    private final UserService userService;

    UserController(UserRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
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

    // tag::get-single-user[]
    @PostMapping("/users")
    EntityModel<UserProfile> one(@RequestBody Map<String, Object> rBody) throws Exception {

        String email = rBody.get("email").toString();
        String password = rBody.get("password").toString();

        UserProfile user = repository.findUserByEmail(email);

        if (user == null || !user.getPassword().equals(password)) {
            throw new Exception("Invalid email or password");
        }

        return EntityModel.of(user, //
                linkTo(methodOn(UserController.class).one(rBody)).withSelfRel(),
                linkTo(methodOn(UserController.class).all()).withRel("users"));
    }
    // end::get-single-user[]

    // tag::signup[]
    @PostMapping("/signup")
    ResponseEntity<?> signUp(@RequestBody UserProfile userData) {

        try {
            userService.signup(userData);
            return ResponseEntity.ok(userData);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new Error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    // end::signup[]
}

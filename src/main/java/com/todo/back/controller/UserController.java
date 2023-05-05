package com.todo.back.controller;

import com.todo.back.model.UserProfile;
import com.todo.back.repository.user.UserRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.todo.back.services.EmailService;

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
    ResponseEntity<?> signUp(@RequestBody UserProfile userData) throws Exception {

        String email = userData.getEmail();
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", Pattern.CASE_INSENSITIVE);
        Matcher emailMatcher = emailPattern.matcher(email);
        boolean emailMatchFound = emailMatcher.find();
        UserProfile emailIsUsed = repository.findUserByEmail(email);

        String name = userData.getName();
        Pattern namePattern = Pattern.compile("^[a-zA-Z]{1,30}$", Pattern.CASE_INSENSITIVE);
        Matcher nameMatcher = namePattern.matcher(name);
        boolean nameMatchFound = nameMatcher.find();

        String surname = userData.getSurname();
        Matcher surnameMatcher = namePattern.matcher(surname);
        boolean surnameMatchFound = surnameMatcher.find();

        String password = userData.getPassword();
        Pattern passwordPattern = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9])(?!.*\\s).{8,}$", Pattern.CASE_INSENSITIVE);
        Matcher passwordMatcher = passwordPattern.matcher(password);
        boolean passwordMatchFound = passwordMatcher.find();

        if (emailIsUsed != null) {
            return ResponseEntity.badRequest().body(new Error("This email has been used already"));
        }

        if (!emailMatchFound) {
            return ResponseEntity.badRequest().body(new Error("The email format is not valid"));
        }

        if (!nameMatchFound) {
            return ResponseEntity.badRequest().body(new Error("The name format is not valid"));
        }

        if (!surnameMatchFound) {
            return ResponseEntity.badRequest().body(new Error("The surname format is not valid"));
        }

        if (!passwordMatchFound) {
            return ResponseEntity.badRequest().body(new Error("The password format is not valid"));
        }

        EmailService.sendmail(email);

        return ResponseEntity.ok(repository.save(userData));
    }
    // end::signup[]
}

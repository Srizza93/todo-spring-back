package com.todo.back.controller;

import com.todo.back.model.UserProfile;
import com.todo.back.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    // tag::get-aggregate-root[]
    @GetMapping("/users")
    public ResponseEntity<?> all() {

        try {
            return ResponseEntity.ok(userService.users());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    // end::get-aggregate-root[]

    // tag::get-single-user[]
    @PostMapping("/users")
    public ResponseEntity<?> one(@RequestBody Map<String, Object> rBody) {

        try {
            return ResponseEntity.ok(userService.login(rBody));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new Error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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

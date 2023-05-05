package com.todo.back.controller;

import com.todo.back.dto.user.UserLoginDto;
import com.todo.back.dto.user.UserSignupDto;
import com.todo.back.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/login")
    public ResponseEntity<?> one(@RequestBody UserLoginDto credentials) {

        try {
            return ResponseEntity.ok(userService.login(credentials));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new Error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    // end::get-single-user[]

    // tag::signup[]
    @PostMapping("/signup")
    ResponseEntity<?> signUp(@RequestBody UserSignupDto userData) {

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

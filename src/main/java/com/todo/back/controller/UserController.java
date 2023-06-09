package com.todo.back.controller;

import com.todo.back.payload.request.LoginRequest;
import com.todo.back.payload.request.SignupRequest;
import com.todo.back.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.todo.back.exception.ControllerExceptionHandler.handleInternalServerException;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers() {

        try {
            return ResponseEntity.ok(userService.users());
        } catch (Exception e) {
            return handleInternalServerException(e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        try {
            return ResponseEntity.ok(userService.login(loginRequest));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new Error(e.getMessage()));
        } catch (Exception e) {
            return handleInternalServerException(e);
        }
    }

    @PostMapping("/signup")
    ResponseEntity<?> signUp(@RequestBody SignupRequest signUpRequest) {

        try {
            userService.signup(signUpRequest);
            return ResponseEntity.ok(signUpRequest);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new Error(e.getMessage()));
        } catch (Exception e) {
            return handleInternalServerException(e);
        }
    }
}

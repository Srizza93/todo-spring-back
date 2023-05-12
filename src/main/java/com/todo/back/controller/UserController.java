package com.todo.back.controller;

import com.todo.back.dto.user.UserLoginDto;
import com.todo.back.model.ERole;
import com.todo.back.model.Role;
import com.todo.back.model.UserProfile;
import com.todo.back.payload.request.LoginRequest;
import com.todo.back.payload.response.JwtResponse;
import com.todo.back.payload.request.SignupRequest;
import com.todo.back.payload.response.MessageResponse;
import com.todo.back.repository.role.RoleRepository;
import com.todo.back.repository.user.UserRepository;
import com.todo.back.security.jwt.JwtUtils;
import com.todo.back.security.services.UserDetailsImpl;
import com.todo.back.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    // tag::get-aggregate-root[]
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {

        try {
            return ResponseEntity.ok(userService.users());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    // end::get-aggregate-root[]

    // tag::get-single-user[]
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        try {
            return ResponseEntity.ok(userService.login(loginRequest));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new Error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    // end::get-single-user[]


    // tag::signup[]
    @PostMapping("/signup")
    ResponseEntity<?> signUp(@RequestBody SignupRequest signUpRequest) {

        try {
            userService.signup(signUpRequest);
            return ResponseEntity.ok(signUpRequest);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new Error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    // end::signup[]
}

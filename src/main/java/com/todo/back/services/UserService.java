package com.todo.back.services;

import com.todo.back.controller.UserController;
import com.todo.back.model.ERole;
import com.todo.back.model.Role;
import com.todo.back.model.UserProfile;
import com.todo.back.payload.request.LoginRequest;
import com.todo.back.payload.request.SignupRequest;
import com.todo.back.payload.response.JwtResponse;
import com.todo.back.payload.response.MessageResponse;
import com.todo.back.repository.RoleRepository;
import com.todo.back.repository.UserRepository;
import com.todo.back.security.jwt.JwtUtils;
import com.todo.back.security.services.UserDetailsImpl;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public CollectionModel<EntityModel<UserProfile>> users() {

        List<EntityModel<UserProfile>> users = userRepository.findAll().stream()
                .map(user -> EntityModel.of(user,
                        linkTo(methodOn(UserController.class).getAllUsers()).withRel("users")))
                .toList();

        return CollectionModel.of(users, linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
    }

    public ResponseEntity<JwtResponse> login(LoginRequest loginRequest) throws IllegalArgumentException {

        String username = loginRequest.getUsername();
        Optional<UserProfile> user = userRepository.findByUsername(username);

        UserProfile userProfile = user.orElseThrow(() -> new IllegalArgumentException("Invalid username"));

        String inputPassword = loginRequest.getPassword();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        boolean passwordIsValid = encoder.matches(inputPassword, userProfile.getPassword());

        if (!passwordIsValid) {
            throw new IllegalArgumentException("Invalid password");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    public ResponseEntity<MessageResponse> signup(SignupRequest signUpRequest) throws IllegalArgumentException, IOException, MessagingException {

        String username = signUpRequest.getUsername();
        Pattern usernamePattern = Pattern.compile("^[a-zA-Z0-9]{3,30}$", Pattern.CASE_INSENSITIVE);
        Matcher usernameMatcher = usernamePattern.matcher(username);
        boolean usernameMatchFound = usernameMatcher.find();
        boolean usernameIsUsed = userRepository.existsByUsername(signUpRequest.getUsername());

        String email = signUpRequest.getEmail();
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", Pattern.CASE_INSENSITIVE);
        Matcher emailMatcher = emailPattern.matcher(email);
        boolean emailMatchFound = emailMatcher.find();
        boolean emailIsUsed = userRepository.existsByEmail(email);

        String name = signUpRequest.getName();
        Matcher nameMatcher = usernamePattern.matcher(name);
        boolean nameMatchFound = nameMatcher.find();

        String surname = signUpRequest.getSurname();
        Matcher surnameMatcher = usernamePattern.matcher(surname);
        boolean surnameMatchFound = surnameMatcher.find();

        String password = signUpRequest.getPassword();
        Pattern passwordPattern = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9])(?!.*\\s).{8,}$", Pattern.CASE_INSENSITIVE);
        Matcher passwordMatcher = passwordPattern.matcher(password);
        boolean passwordMatchFound = passwordMatcher.find();

        if (usernameIsUsed) {
            throw new IllegalArgumentException("This username has been used already");
        }

        if (!usernameMatchFound) {
            throw new IllegalArgumentException("The username format is not valid");
        }

        if (emailIsUsed) {
            throw new IllegalArgumentException("This email has been used already");
        }

        if (!emailMatchFound) {
            throw new IllegalArgumentException("The email format is not valid");
        }

        if (!nameMatchFound) {
            throw new IllegalArgumentException("The name format is not valid");
        }

        if (!surnameMatchFound) {
            throw new IllegalArgumentException("The surname format is not valid");
        }

        if (!passwordMatchFound) {
            throw new IllegalArgumentException("The password format is not valid");
        }

        // Create new user's account
        UserProfile user = new UserProfile(username, name, surname, email, encoder.encode(password));

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin" -> {
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                    }
                    case "mod" -> {
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
                    }
                    default -> {
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                    }
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        try {
            EmailService.sendmail(email);
        } catch (Exception e) {
            throw new MessagingException("Error: couldn't send the registration email" + e);
        }

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

}

package com.todo.back.services;

import com.todo.back.controller.UserController;
import com.todo.back.model.ERole;
import com.todo.back.model.UserProfile;
import com.todo.back.payload.request.LoginRequest;
import com.todo.back.payload.request.SignupRequest;
import com.todo.back.payload.response.JwtResponse;
import com.todo.back.repository.UserRepository;
import com.todo.back.security.jwt.JwtUtils;
import com.todo.back.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
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
    private final EmailService emailService;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder encoder;

    UserService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public CollectionModel<EntityModel<UserProfile>> users() throws UserServiceException {

        try {
            List<EntityModel<UserProfile>> users = userRepository.findAll().stream()
                    .map(user -> EntityModel.of(user,
                            linkTo(methodOn(UserController.class).getAllUsers()).withRel("users")))
                    .toList();

            return CollectionModel.of(users, linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
        } catch (Exception e) {
            throw new UserServiceException("Failed to fetch users");
        }
    }

    public ResponseEntity<JwtResponse> login(LoginRequest loginRequest) throws UserServiceException, IllegalArgumentException {

        try {
            String username = loginRequest.getUsername();
            Optional<UserProfile> user = userRepository.findByUsername(username);

            UserProfile userProfile = user.orElseThrow(() -> new IllegalArgumentException("Invalid username"));

            String inputPassword = loginRequest.getPassword();
            boolean passwordIsValid = encoder.matches(inputPassword, userProfile.getPassword());

            if (!passwordIsValid) {
                throw new IllegalArgumentException("Invalid password");
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Set<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toSet());

            return ResponseEntity.ok(new JwtResponse(jwt,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    roles));
        } catch (Exception e) {
            throw new UserServiceException(e.getMessage());
        }
    }

    public void signup(SignupRequest signUpRequest) throws UserServiceException, IllegalArgumentException {

        try {
            String username = signUpRequest.getUsername();
            Pattern usernamePattern = Pattern.compile("^[a-zA-Z0-9]{3,30}$", Pattern.CASE_INSENSITIVE);
            Matcher usernameMatcher = usernamePattern.matcher(username);
            boolean usernameMatchFound = usernameMatcher.find();
            boolean usernameIsUsed = userRepository.existsByUsername(username);

            String email = signUpRequest.getEmail();
            Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", Pattern.CASE_INSENSITIVE);
            Matcher emailMatcher = emailPattern.matcher(email);
            boolean emailMatchFound = emailMatcher.find();
            boolean emailIsUsed = userRepository.existsByEmail(email);

            String name = signUpRequest.getName();
            Pattern namePattern = Pattern.compile("^[a-zA-Z]{1,30}( [a-zA-Z]{1,30}){0,3}$", Pattern.CASE_INSENSITIVE);
            Matcher nameMatcher = namePattern.matcher(name);
            boolean nameMatchFound = nameMatcher.find();

            String surname = signUpRequest.getSurname();
            Matcher surnameMatcher = namePattern.matcher(surname);
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

            UserProfile user = new UserProfile(username, name, surname, email, encoder.encode(password));

            Set<String> strRoles = signUpRequest.getRoles();
            Set<String> roles = new HashSet<>();

            if (strRoles.size() == 0) {
                roles.add(ERole.userRole);
            } else {
                strRoles.forEach(role -> {
                    switch (role) {
                        case "admin" -> {
                            roles.add(ERole.adminRole);
                        }
                        case "mod" -> {
                            roles.add(ERole.moderatorRole);
                        }
                        default -> {
                            roles.add(ERole.userRole);
                        }
                    }
                });
            }

            user.setRoles(roles);
            userRepository.save(user);

            try {
                emailService.sendmail(email);
            } catch (Exception e) {
                throw new MessagingException(e.getMessage());
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (Exception e) {
            throw new UserServiceException("Failed to signup " + e.getMessage());
        }
    }

}
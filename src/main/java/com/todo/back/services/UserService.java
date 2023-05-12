package com.todo.back.services;

import com.todo.back.controller.UserController;
import com.todo.back.dto.user.UserSignupDto;
import com.todo.back.model.UserProfile;
import com.todo.back.payload.request.LoginRequest;
import com.todo.back.payload.response.JwtResponse;
import com.todo.back.repository.user.UserRepository;
import com.todo.back.security.jwt.JwtUtils;
import com.todo.back.security.services.UserDetailsImpl;
import jakarta.mail.MessagingException;
import org.apache.catalina.User;
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
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
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

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public CollectionModel<EntityModel<UserProfile>> users() {

        List<EntityModel<UserProfile>> users = userRepository.findAll().stream()
                .map(user -> EntityModel.of(user,
                        linkTo(methodOn(UserController.class).all()).withRel("users")))
                .toList();

        return CollectionModel.of(users, linkTo(methodOn(UserController.class).all()).withSelfRel());
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

        System.out.println(authentication + " - JWT " + jwt);

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

    public void signup(UserSignupDto userDataDto) throws IllegalArgumentException, IOException, MessagingException {

        String email = userDataDto.getEmail();
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", Pattern.CASE_INSENSITIVE);
        Matcher emailMatcher = emailPattern.matcher(email);
        boolean emailMatchFound = emailMatcher.find();
        UserProfile emailIsUsed = userRepository.findUserByEmail(email);

        String username = userDataDto.getName();
        Pattern usernamePattern = Pattern.compile("^[a-zA-Z]{1,30}$", Pattern.CASE_INSENSITIVE);
        Matcher usernameMatcher = usernamePattern.matcher(username);
        boolean usernameMatchFound = usernameMatcher.find();

        String name = userDataDto.getName();
        Matcher nameMatcher = usernamePattern.matcher(name);
        boolean nameMatchFound = nameMatcher.find();

        String surname = userDataDto.getSurname();
        Matcher surnameMatcher = usernamePattern.matcher(surname);
        boolean surnameMatchFound = surnameMatcher.find();

        String password = userDataDto.getPassword();
        Pattern passwordPattern = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9])(?!.*\\s).{8,}$", Pattern.CASE_INSENSITIVE);
        Matcher passwordMatcher = passwordPattern.matcher(password);
        boolean passwordMatchFound = passwordMatcher.find();

        if (emailIsUsed != null) {
            throw new IllegalArgumentException("This email has been used already");
        }

        if (!emailMatchFound) {
            throw new IllegalArgumentException("The email format is not valid");
        }

        if (!usernameMatchFound) {
            throw new IllegalArgumentException("The username format is not valid");
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

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        String encodedPassword = encoder.encode(password);
        userDataDto.setPassword(encodedPassword);

        UserProfile userData = new UserProfile(username, name, surname, email, encodedPassword);
        userData.setEmail(userDataDto.getEmail());
        userData.setUsername(userDataDto.getUsername());
        userData.setName(userDataDto.getName());
        userData.setSurname(userDataDto.getSurname());
        userData.setPassword(userDataDto.getPassword());

        try {
            EmailService.sendmail(email);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        userRepository.save(userData);

    }

}

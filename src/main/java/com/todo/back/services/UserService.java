package com.todo.back.services;

import com.todo.back.controller.UserController;
import com.todo.back.dto.user.UserLoginDto;
import com.todo.back.dto.user.UserSignupDto;
import com.todo.back.model.UserProfile;
import com.todo.back.repository.user.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class UserService {

    private final UserRepository userRepository;

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

//    public EntityModel<UserProfile> login(UserLoginDto credentials) throws IllegalArgumentException {
//
//        String email = credentials.getEmail();
//        UserProfile user = userRepository.findUserByEmail(email);
//
//        if (user == null) {
//            throw new IllegalArgumentException("Invalid email");
//        }
//
//        String inputPassword = credentials.getPassword();
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
//        boolean passwordIsValid = encoder.matches(inputPassword, user.getPassword());
//
//        if (!passwordIsValid) {
//            throw new IllegalArgumentException("Invalid password");
//        }
//
//        return EntityModel.of(user, //
//                linkTo(methodOn(UserController.class).one(credentials)).withSelfRel(),
//                linkTo(methodOn(UserController.class).all()).withRel("users"));
//    }

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

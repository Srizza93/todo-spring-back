package com.todo.back.services;

import com.todo.back.model.UserProfile;
import com.todo.back.repository.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {

    private final UserRepository userRepository;

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void signup(UserProfile userData) throws Exception {

        String email = userData.getEmail();
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", Pattern.CASE_INSENSITIVE);
        Matcher emailMatcher = emailPattern.matcher(email);
        boolean emailMatchFound = emailMatcher.find();
        UserProfile emailIsUsed = userRepository.findUserByEmail(email);

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

        EmailService.sendmail(email);
        userRepository.save(userData);

    }

}

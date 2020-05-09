package com.piisw.cinema_tickets_app.domain.user.control;

import com.piisw.cinema_tickets_app.api.UserDTO;
import com.piisw.cinema_tickets_app.domain.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserService {

    public static final String USERNAME_ALREADY_TAKEN_MSG = "User with username {0} already exists!";
    public static final String EMAIL_ALREADY_TAKEN_MSG = "User with email {0} already exists!";
    private static final String NO_SUCH_USER = "There is no user with id {0}";
    public static final String PASSWORD_NOT_PASSED_VALIDATION_RULES = "Password not passed validation rules";
    private static final Pattern EMAIL_REGEX = Pattern.compile("[^@ ]+@[^@ ]+\\.[^@ ]+");
    public static final String EMAIL_INCORRECT = "Supplied email is incorrect";

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public User registerUser(User newUser) {
        validateUsernameUniqueness(newUser);
        validateEmailCorrectness(newUser.getEmail());
        validateEmailUniqueness(newUser.getEmail());
        validatePasswordRules(newUser.getPassword());

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        return userRepository.save(newUser);
    }

    private void validateUsernameUniqueness(User user) {
        if(userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException(MessageFormat.format(USERNAME_ALREADY_TAKEN_MSG, user.getUsername()));
        }
    }

    private void validateEmailUniqueness(String email) {
        if(userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(MessageFormat.format(EMAIL_ALREADY_TAKEN_MSG, email));
        }
    }

    private void validateEmailCorrectness(String email) {
        if (!EMAIL_REGEX.matcher(email).matches()) {
            throw new IllegalArgumentException(EMAIL_INCORRECT);
        }
    }

    private void validatePasswordRules(String password) {
        if (password.length() < 8) {
            throw new IllegalArgumentException(PASSWORD_NOT_PASSED_VALIDATION_RULES);
        }
    }

    public User getExistingUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(NO_SUCH_USER, userId)));
    }

    public List<User> getUsersFromDatabase(List<Long> usersId){
        return userRepository.findAllById(usersId);
    }

    public boolean userExistsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public UserDTO mapToUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getUserRole().name())
                .build();
    }

    public void setNewPassword(User user, String newPassword) {
        validatePasswordRules(newPassword);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void setNewEmail(User user, String email) {
        validateEmailCorrectness(email);
        validateEmailUniqueness(email);
        user.setEmail(email);
        userRepository.save(user);
    }

    public void setNewPhoneNumber(User user, String phoneNumber) {
        user.setPhoneNumber(phoneNumber);
        userRepository.save(user);
    }
}

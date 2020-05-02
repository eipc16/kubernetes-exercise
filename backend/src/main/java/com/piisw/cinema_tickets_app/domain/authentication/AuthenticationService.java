package com.piisw.cinema_tickets_app.domain.authentication;

import com.piisw.cinema_tickets_app.api.LoginDataDTO;
import com.piisw.cinema_tickets_app.api.RegistrationDataDTO;
import com.piisw.cinema_tickets_app.api.TokenDTO;
import com.piisw.cinema_tickets_app.domain.user.User;
import com.piisw.cinema_tickets_app.domain.user.UserService;
import com.piisw.cinema_tickets_app.infrastructure.security.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationService {

    @Autowired
    private UserService userService;

    public UsernamePasswordAuthenticationToken getAuthenticationToken(LoginDataDTO loginDTO) {
        return new UsernamePasswordAuthenticationToken(loginDTO.getUsernameOrEmail(), loginDTO.getPassword());
    }

    public UsernamePasswordAuthenticationToken getAuthenticationToken(String username, String password) {
        return new UsernamePasswordAuthenticationToken(username, password);
    }

    public TokenDTO mapToTokenDTO(String token) {
        return new TokenDTO(token);
    }

    public User createUserBasedOnRegistrationData(RegistrationDataDTO registrationData) {
        User newUser = User.builder()
                .name(registrationData.getName())
                .surname(registrationData.getSurname())
                .username(registrationData.getUsername())
                .password(registrationData.getPassword())
                .email(registrationData.getEmail())
                .phoneNumber(registrationData.getPhoneNumber())
                .userRole(UserRole.ROLE_USER)
                .build();
        return userService.registerUser(newUser);
    }

}

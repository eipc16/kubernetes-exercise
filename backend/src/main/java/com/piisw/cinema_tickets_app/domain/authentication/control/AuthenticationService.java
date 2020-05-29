package com.piisw.cinema_tickets_app.domain.authentication.control;

import com.piisw.cinema_tickets_app.api.LoginDataDTO;
import com.piisw.cinema_tickets_app.api.RegistrationDataDTO;
import com.piisw.cinema_tickets_app.api.TokenDTO;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.user.entity.User;
import com.piisw.cinema_tickets_app.domain.user.control.UserService;
import com.piisw.cinema_tickets_app.infrastructure.security.UserInfo;
import com.piisw.cinema_tickets_app.infrastructure.security.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class AuthenticationService {

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
                .objectState(ObjectState.ACTIVE)
                .build();
        return userService.registerUser(newUser);
    }

    public boolean hasRole(UserInfo userInfo, UserRole userRole) {
        return userInfo.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(userRole.name()::equals);
    }

}

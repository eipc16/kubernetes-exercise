package com.piisw.cinema_tickets_app.domain.authentication;

import com.piisw.cinema_tickets_app.api.LoginDataDTO;
import com.piisw.cinema_tickets_app.api.PasswordConfirmedDataDTO;
import com.piisw.cinema_tickets_app.api.RegistrationDataDTO;
import com.piisw.cinema_tickets_app.api.ResourceDTO;
import com.piisw.cinema_tickets_app.api.ResponseDTO;
import com.piisw.cinema_tickets_app.api.TokenDTO;
import com.piisw.cinema_tickets_app.domain.user.User;
import com.piisw.cinema_tickets_app.domain.user.UserService;
import com.piisw.cinema_tickets_app.infrastructure.security.TokenHandler;
import com.piisw.cinema_tickets_app.infrastructure.security.UserInfo;
import com.piisw.cinema_tickets_app.infrastructure.security.validation.HasAnyRole;
import com.piisw.cinema_tickets_app.infrastructure.security.validation.LoggedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenHandler tokenHandler;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    @PostMapping("/signin")
    public TokenDTO authenticateUser(@Valid @RequestBody LoginDataDTO loginData) {
        Authentication authentication = authenticationManager.authenticate(authenticationService.getAuthenticationToken(loginData));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenHandler.generateToken(authentication);
        return authenticationService.mapToTokenDTO(token);
    }

    @PostMapping("/signup")
    public ResourceDTO registerUser(@Valid @RequestBody RegistrationDataDTO registrationData) {
        User newUser = authenticationService.createUserBasedOnRegistrationData(registrationData);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/users/{userId}")
                .buildAndExpand(newUser.getId())
                .toUri();
        return ResourceDTO.builder()
                .id(newUser.getId())
                .identifier(newUser.getUsername())
                .uri(uri)
                .build();
    }

    @PostMapping("/change-password")
    @HasAnyRole
    public ResponseDTO changePassword(@Valid @RequestBody PasswordConfirmedDataDTO passwordChange, @LoggedUser UserInfo userInfo) {
        authenticationManager.authenticate(authenticationService.getAuthenticationToken(userInfo.getUsername(), passwordChange.getPassword()));
        User user = userService.getExistingUser(userInfo.getId());
        userService.setNewPassword(user, passwordChange.getValue());
        return ResponseDTO.builder()
                .success(true)
                .build();
    }

    @PostMapping("/change-email")
    @HasAnyRole
    public ResponseDTO changeEmail(@Valid @RequestBody PasswordConfirmedDataDTO emailChange, @LoggedUser UserInfo userInfo) {
        authenticationManager.authenticate(authenticationService.getAuthenticationToken(userInfo.getUsername(), emailChange.getPassword()));
        User user = userService.getExistingUser(userInfo.getId());
        userService.setNewEmail(user, emailChange.getValue());
        return ResponseDTO.builder()
                .success(true)
                .build();
    }

}



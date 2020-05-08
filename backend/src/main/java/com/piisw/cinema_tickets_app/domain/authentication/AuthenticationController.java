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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

@Api(tags = "Authentication")
@RestController
@RequestMapping(AuthenticationController.MAIN_PATH)
public class AuthenticationController {

    public static final String MAIN_PATH = "/auth";

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenHandler tokenHandler;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Login", notes = "Allows login to system. Returns Json Web Token required to use api.\n"
            + "When performing request on secured endpoint following header must be attached:\n"
            + "<i>Authorization: Bearer <token></i>\n"
            + "For authorization in swagger click authorize button and paste in text field returned token prefixed with <i>Bearer</>, e.g: \n"
            + "\"Bearer &lt token &gt\"")
    @PostMapping("/signin")
    public TokenDTO authenticateUser(@Valid @RequestBody LoginDataDTO loginData) {
        Authentication authentication = authenticationManager.authenticate(authenticationService.getAuthenticationToken(loginData));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenHandler.generateToken(authentication);
        return authenticationService.mapToTokenDTO(token);
    }

    @ApiOperation(value = "Register user", notes = "Allows to register new user based on supplied registration data.")
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

    @ApiOperation(value = "Change password", notes = "Changes password for current user. New password must be confirmed with current password.")
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

    @ApiOperation(value = "Change email", notes = "Changes email of current user. New email must be confirmed with password.")
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



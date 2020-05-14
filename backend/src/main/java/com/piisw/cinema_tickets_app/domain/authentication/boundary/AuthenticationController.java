package com.piisw.cinema_tickets_app.domain.authentication.boundary;

import com.piisw.cinema_tickets_app.api.LoginDataDTO;
import com.piisw.cinema_tickets_app.api.PasswordConfirmedDataDTO;
import com.piisw.cinema_tickets_app.api.RegistrationDataDTO;
import com.piisw.cinema_tickets_app.api.ResourceDTO;
import com.piisw.cinema_tickets_app.api.ResponseDTO;
import com.piisw.cinema_tickets_app.api.TokenDTO;
import com.piisw.cinema_tickets_app.domain.authentication.control.AuthenticationService;
import com.piisw.cinema_tickets_app.domain.user.boundary.UserMapper;
import com.piisw.cinema_tickets_app.domain.user.entity.User;
import com.piisw.cinema_tickets_app.domain.user.control.UserService;
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
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

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

    @Autowired
    private UserMapper userMapper;

    @ApiOperation(value = "${api.auth.signin.value}", notes = "${api.auth.signin.notes}")
    @PostMapping("/signin")
    public TokenDTO authenticateUser(@Valid @RequestBody LoginDataDTO loginData) {
        Authentication authentication = authenticationManager.authenticate(authenticationService.getAuthenticationToken(loginData));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenHandler.generateToken(authentication);
        return authenticationService.mapToTokenDTO(token);
    }

    @ApiOperation(value = "${api.auth.signup.value}", notes = "${api.auth.signup.notes}")
    @PostMapping("/signup")
    public ResourceDTO registerUser(@Valid @RequestBody RegistrationDataDTO registrationData) {
        User newUser = authenticationService.createUserBasedOnRegistrationData(registrationData);
        return userMapper.mapToResourceDTO(newUser);
    }

    @ApiOperation(value = "${api.auth.change.password.value}", notes = "${api.auth.change.password.notes}")
    @PostMapping("/change-password")
    @HasAnyRole
    public ResponseDTO changePassword(@Valid @RequestBody PasswordConfirmedDataDTO passwordChange, @ApiIgnore @LoggedUser UserInfo userInfo) {
        authenticationManager.authenticate(authenticationService.getAuthenticationToken(userInfo.getUsername(), passwordChange.getPassword()));
        User user = userService.getExistingUser(userInfo.getId());
        userService.setNewPassword(user, passwordChange.getValue());
        return ResponseDTO.builder()
                .success(true)
                .build();
    }

    @ApiOperation(value = "${api.auth.change.email.value}", notes = "${api.auth.change.email.notes}")
    @PostMapping("/change-email")
    @HasAnyRole
    public ResponseDTO changeEmail(@Valid @RequestBody PasswordConfirmedDataDTO emailChange, @ApiIgnore @LoggedUser UserInfo userInfo) {
        authenticationManager.authenticate(authenticationService.getAuthenticationToken(userInfo.getUsername(), emailChange.getPassword()));
        User user = userService.getExistingUser(userInfo.getId());
        userService.setNewEmail(user, emailChange.getValue());
        return ResponseDTO.builder()
                .success(true)
                .build();
    }

}



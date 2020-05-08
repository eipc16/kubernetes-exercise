package com.piisw.cinema_tickets_app.domain.user;

import com.piisw.cinema_tickets_app.api.AvailableDTO;
import com.piisw.cinema_tickets_app.api.UserDTO;
import com.piisw.cinema_tickets_app.infrastructure.security.UserInfo;
import com.piisw.cinema_tickets_app.infrastructure.security.validation.HasAnyRole;
import com.piisw.cinema_tickets_app.infrastructure.security.validation.LoggedUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Api(tags = "Users")
@RestController
@RequestMapping(UserController.MAIN_PATH)
public class UserController {

    @Autowired
    private UserService userService;

    public static final String MAIN_PATH = "/users";
    private static final String USERNAME = "username";
    private static final String EMAIL = "email";
    private static final String CHECK_USERNAME_PATH = "/check/username/{" + USERNAME + "}";
    private static final String CHECK_EMAIL_PATH = "/check/email/{" + EMAIL + "}";
    private static final String ID = "id";
    private static final String ID_PATH = "/{" + ID + "}";

    @ApiOperation(value = "Check username availability", notes = "Checks if given username is available.")
    @GetMapping(CHECK_USERNAME_PATH)
    public AvailableDTO checkUsernameAvailability(@PathVariable(USERNAME) String username) {
        Boolean isAvailable = !userService.userExistsByUsername(username);
        return new AvailableDTO(isAvailable);
    }

    @ApiOperation(value = "Check email availability", notes = "Checks if given email is available.")
    @GetMapping(CHECK_EMAIL_PATH)
    public AvailableDTO checkIfEmailAvailability(@PathVariable(EMAIL) String email) {
        Boolean isAvailable = !userService.userExistsByEmail(email);
        return new AvailableDTO(isAvailable);
    }

    @ApiOperation(value = "Get user by id", notes = "Returns user with given id.")
    @GetMapping(ID_PATH)
    @HasAnyRole
    public UserDTO getUser(@PathVariable(ID) Long id) {
        User user = userService.getExistingUser(id);
        return userService.mapToUserDTO(user);
    }

    @ApiOperation(value = "Get current user", notes = "Returns information about currently logged user.")
    @GetMapping("/current")
    @HasAnyRole
    public UserDTO getCurrentUser(@LoggedUser UserInfo currentUserInfo) {
        User user = userService.getExistingUser(currentUserInfo.getId());
        return userService.mapToUserDTO(user);
    }

}

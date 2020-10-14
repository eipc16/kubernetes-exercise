package com.piisw.cinema_tickets_app.domain.user.boundary;

import com.piisw.cinema_tickets_app.api.AvailableDTO;
import com.piisw.cinema_tickets_app.api.UserDTO;
import com.piisw.cinema_tickets_app.domain.user.control.UserService;
import com.piisw.cinema_tickets_app.domain.user.entity.UserEntity;
import com.piisw.cinema_tickets_app.infrastructure.security.UserInfo;
import com.piisw.cinema_tickets_app.infrastructure.security.validation.HasAnyRole;
import com.piisw.cinema_tickets_app.infrastructure.security.validation.LoggedUser;
import static com.piisw.cinema_tickets_app.infrastructure.utils.ResourcePath.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;


@Api(tags = "Users")
@RestController
@RequestMapping(UserController.MAIN_RESOURCE)
@RequiredArgsConstructor
public class UserController {

    public static final String MAIN_RESOURCE = "/users";
    private static final String USERNAME = "username";
    private static final String EMAIL = "email";
    private static final String CHECK_USERNAME_PATH = "/check/username/{" + USERNAME + "}";
    private static final String CHECK_EMAIL_PATH = "/check/email/{" + EMAIL + "}";

    private final UserService userService;
    private final UserMapper userMapper;

    @ApiOperation(value = "${api.users.check.username.value}", notes = "${api.users.check.username.notes}")
    @GetMapping(CHECK_USERNAME_PATH)
    public AvailableDTO checkUsernameAvailability(@PathVariable(USERNAME) String username) {
        Boolean isAvailable = !userService.userExistsByUsername(username);
        return new AvailableDTO(isAvailable);
    }

    @ApiOperation(value = "${api.users.check.email.value}", notes = "${api.users.check.email.notes}")
    @GetMapping(CHECK_EMAIL_PATH)
    public AvailableDTO checkIfEmailAvailability(@PathVariable(EMAIL) String email) {
        Boolean isAvailable = !userService.userExistsByEmail(email);
        return new AvailableDTO(isAvailable);
    }

    @ApiOperation(value = "${api.users.get.value}", notes = "${api.users.get.notes}")
    @GetMapping(ID_PATH)
    @HasAnyRole
    public UserDTO getUser(@PathVariable(ID) Long id) {
        UserEntity user = userService.getExistingUser(id);
        return userMapper.mapToUserDTO(user);
    }

    @ApiOperation(value = "${api.users.current.value}", notes = "${api.users.current.notes}")
    @GetMapping("/current")
    @HasAnyRole
    public UserDTO getCurrentUser(@ApiIgnore @LoggedUser UserInfo currentUserInfo) {
        UserEntity user = userService.getExistingUser(currentUserInfo.getId());
        return userMapper.mapToUserDTO(user);
    }

}

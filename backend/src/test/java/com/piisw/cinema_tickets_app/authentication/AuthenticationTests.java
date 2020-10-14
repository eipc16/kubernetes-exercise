package com.piisw.cinema_tickets_app.authentication;

import com.piisw.cinema_tickets_app.api.RegistrationDataDTO;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.authentication.control.AuthenticationService;
import com.piisw.cinema_tickets_app.domain.user.entity.UserEntity;
import com.piisw.cinema_tickets_app.domain.user.control.UserService;
import com.piisw.cinema_tickets_app.infrastructure.configuration.AuditingConfig;
import com.piisw.cinema_tickets_app.infrastructure.security.UserRole;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.MessageFormat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import({AuthenticationService.class, UserService.class, BCryptPasswordEncoder.class, AuditingConfig.class})
public class AuthenticationTests {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void shouldRegisterNewUser() {
        RegistrationDataDTO registrationData = getDummyRegistrationData();
        UserEntity createdUser = authenticationService.createUserBasedOnRegistrationData(registrationData);
        assertEquals(registrationData.getName(), createdUser.getName());
        assertEquals(registrationData.getSurname(), createdUser.getSurname());
        assertEquals(registrationData.getUsername(), createdUser.getUsername());
        assertEquals(registrationData.getPhoneNumber(), createdUser.getPhoneNumber());
        assertEquals(registrationData.getEmail(), createdUser.getEmail());
        assertEquals(UserRole.ROLE_USER, createdUser.getUserRole());
        assertEquals(ObjectState.ACTIVE, createdUser.getObjectState());
    }

    private RegistrationDataDTO getDummyRegistrationData() {
        return RegistrationDataDTO.builder()
                .name("Johnny")
                .surname("Bravo")
                .username("Johnny123")
                .email("johnny.bravo@examle.com")
                .password("weakPassword123")
                .phoneNumber("123456789")
                .build();
    }

    @Test
    public void shouldThrowExceptionOnTooShortPassword() {
        RegistrationDataDTO registrationData = getDummyRegistrationData();
        registrationData.setPassword("123");
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage(UserService.PASSWORD_NOT_PASSED_VALIDATION_RULES);
        authenticationService.createUserBasedOnRegistrationData(registrationData);
    }

    @Test
    public void shouldThrowExceptionOnInvalidEmail() {
        RegistrationDataDTO registrationData = getDummyRegistrationData();
        registrationData.setEmail("malformed");
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage(UserService.EMAIL_INCORRECT);
        authenticationService.createUserBasedOnRegistrationData(registrationData);
    }

    @Test
    public void shouldThrowExceptionOnNonAvailableEmail() {
        RegistrationDataDTO registrationData = getDummyRegistrationData();
        authenticationService.createUserBasedOnRegistrationData(registrationData);
        RegistrationDataDTO registrationData2 = getDummyRegistrationData();
        registrationData2.setUsername("differentUsername");
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage(MessageFormat.format(UserService.EMAIL_ALREADY_TAKEN_MSG, registrationData2.getEmail()));
        authenticationService.createUserBasedOnRegistrationData(registrationData2);
    }

    @Test
    public void shouldThrowExceptionOnNonAvailableUsername() {
        RegistrationDataDTO registrationData = getDummyRegistrationData();
        authenticationService.createUserBasedOnRegistrationData(registrationData);
        RegistrationDataDTO registrationData2 = getDummyRegistrationData();
        registrationData2.setEmail("differentEmail@examle.com");
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage(MessageFormat.format(UserService.USERNAME_ALREADY_TAKEN_MSG, registrationData2.getUsername()));
        authenticationService.createUserBasedOnRegistrationData(registrationData2);
    }

    @Test
    public void shouldReturnFalseWhenEmailIsNonAvailable() {
        RegistrationDataDTO registrationData = getDummyRegistrationData();
        authenticationService.createUserBasedOnRegistrationData(registrationData);
        assertTrue(userService.userExistsByEmail(registrationData.getEmail()));
    }

    @Test
    public void shouldReturnFalseWhenUsernameIsNonAvailable() {
        RegistrationDataDTO registrationData = getDummyRegistrationData();
        authenticationService.createUserBasedOnRegistrationData(registrationData);
        assertTrue(userService.userExistsByUsername(registrationData.getUsername()));
    }

}

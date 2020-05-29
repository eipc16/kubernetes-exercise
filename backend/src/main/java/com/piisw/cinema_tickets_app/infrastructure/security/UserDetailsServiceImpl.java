package com.piisw.cinema_tickets_app.infrastructure.security;

import com.piisw.cinema_tickets_app.domain.user.entity.User;
import com.piisw.cinema_tickets_app.domain.user.control.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

@Service
@Transactional
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final String USER_WITH_LOGIN_NOT_FOUND_MSG = "User with login {0} not found";
    private static final String USER_WITH_ID_NOT_FOUND = "User with id {0} not found";

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException(MessageFormat.format(USER_WITH_LOGIN_NOT_FOUND_MSG, usernameOrEmail)));
        return UserInfo.fromUser(user);
    }

    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(MessageFormat.format(USER_WITH_ID_NOT_FOUND, id)));
        return UserInfo.fromUser(user);
    }

}


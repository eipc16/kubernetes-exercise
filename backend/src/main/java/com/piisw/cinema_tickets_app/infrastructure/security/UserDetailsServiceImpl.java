package com.piisw.cinema_tickets_app.infrastructure.security;

import com.piisw.cinema_tickets_app.domain.user.User;
import com.piisw.cinema_tickets_app.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    private static final String USER_WITH_LOGIN_NOT_FOUND_MSG = "User with login {0} not found";
    private static final String USER_WITH_ID_NOT_FOUND = "User with id {0} not found";

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail) {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException(MessageFormat.format(USER_WITH_LOGIN_NOT_FOUND_MSG, usernameOrEmail)));
        return UserInfo.fromUser(user);
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(MessageFormat.format(USER_WITH_ID_NOT_FOUND, id)));
        return UserInfo.fromUser(user);
    }

}


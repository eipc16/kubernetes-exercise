package com.piisw.cinema_tickets_app.infrastructure.configuration;


import com.piisw.cinema_tickets_app.domain.user.User;
import com.piisw.cinema_tickets_app.domain.user.UserService;
import com.piisw.cinema_tickets_app.infrastructure.security.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class AuditingConfig {

    @Autowired
    private UserService userService;

    @Bean
    public AuditorAware<User> auditorProvider() {
        return new SpringSecurityAuditAwareImpl();
    }

    class SpringSecurityAuditAwareImpl implements AuditorAware<User> {

        @Override
        public Optional<User> getCurrentAuditor() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
                return Optional.empty();
            }

            UserInfo userInfo = (UserInfo) authentication.getPrincipal();
            return Optional.ofNullable(userInfo.getId())
                    .map(userService::getExistingUser);
        }
    }

}

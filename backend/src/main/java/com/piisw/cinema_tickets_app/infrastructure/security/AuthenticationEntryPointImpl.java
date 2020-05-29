package com.piisw.cinema_tickets_app.infrastructure.security;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationEntryPointImpl.class);
    private static final String UNAUTHORIZED_LOG_MESSAGE = "Responding with unauthorized error: {0}";

    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException {
        logger.error(UNAUTHORIZED_LOG_MESSAGE, e.getMessage());
        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
    }
}

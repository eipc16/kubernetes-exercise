package com.piisw.cinema_tickets_app.infrastructure.security;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenHandler {

    private static final Logger logger = LoggerFactory.getLogger(TokenHandler.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    public String generateToken(Authentication authentication) {
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(Long.toString(userInfo.getId()))
                .setIssuedAt(new Date())
                .setExpiration(getTokenExpirationDate())
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    private Date getTokenExpirationDate() {
        Date currentDate = new Date();
        return new Date(currentDate.getTime() + jwtExpirationInMs);
    }

    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT accessToken");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT accessToken");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT accessToken");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }
}

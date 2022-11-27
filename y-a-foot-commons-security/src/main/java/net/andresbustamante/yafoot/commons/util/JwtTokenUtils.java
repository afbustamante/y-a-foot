package net.andresbustamante.yafoot.commons.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Token utility operations.
 */
@Component
public class JwtTokenUtils {

    /**
     * JWT secret.
     */
    @Value("${app.security.jwt.token.secret}")
    private String secret;

    /**
     * Token validity limit.
     */
    @Value("${app.security.jwt.token.active.minutes}")
    private Long tokenMaxTime;

    /**
     * Extracts the username from a given token.
     *
     * @param token Token to check
     * @return Username from token
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date from a given token.
     *
     * @param token Token to check
     * @return Expiration date
     */
    public LocalDateTime getExpirationDateFromToken(String token) {
        return DateUtils.toLocalDateTime(getClaimFromToken(token, Claims::getExpiration));
    }

    /**
     * Gets a new token for a given username.
     *
     * @param username Username to use for the token
     * @return New JWT token
     */
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return generateToken(claims, username);
    }

    /**
     * Checks whether a given token is still valid.
     *
     * @param token Token to check
     * @param username Username to compare with
     * @return Whether the token is valid or not
     */
    public boolean isValidToken(String token, String username) {
        String tokenUsername = getUsernameFromToken(token);
        return tokenUsername.equals(username) && !isTokenExpired(token);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token) {
        LocalDateTime expiration = getExpirationDateFromToken(token);
        return expiration.isBefore(LocalDateTime.now());
    }

    private String generateToken(Map<String, Object> claims, String subject) {
        ZonedDateTime now = ZonedDateTime.now();

        return Jwts.builder().setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(now.plusMinutes(tokenMaxTime).toInstant()))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }
}

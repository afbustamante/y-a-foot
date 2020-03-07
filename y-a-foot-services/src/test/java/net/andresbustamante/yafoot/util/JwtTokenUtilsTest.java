package net.andresbustamante.yafoot.util;

import io.jsonwebtoken.ExpiredJwtException;
import net.andresbustamante.yafoot.config.JwtTestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JwtTestConfig.class)
class JwtTokenUtilsTest {

    private static final String EXPIRED_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwZXRlLnNhbXByYXNAZW1haWwuY29tIiwiZXhwI" +
            "joxNTgzNTk1MTQ4LCJpYXQiOjE1ODM1OTE1NDh9.DWT_uyluFJkZAaC39OiytIRIJpGnjqriisBau-lq-xAzRZBRdr_Ks_HGBQcQfXZ" +
            "hkmVdX89s_bVXcDvKVn-mGQ";

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Value("${jwt.token.active.minutes}")
    private Long tokenMaxTime;

    @Test
    void testGetUsernameFromToken() {
        // Given
        String username = "roger.federer@email.com";
        String token = jwtTokenUtils.generateToken(username);

        // When
        String tokenUsername = jwtTokenUtils.getUsernameFromToken(token);

        // Then
        assertNotNull(username);
        assertEquals(username, tokenUsername);
    }

    @Test
    void testGetExpirationDateFromActiveToken() {
        try {
            // Given
            String username = "roger.federer@email.com";
            String token = jwtTokenUtils.generateToken(username);
            LocalDateTime now = LocalDateTime.now();

            // When
            LocalDateTime expirationDate = jwtTokenUtils.getExpirationDateFromToken(token);

            // Then
            assertNotNull(expirationDate);
            assertTrue(now.plusSeconds(tokenMaxTime * 60 + 1).isAfter(expirationDate));
        } catch (ExpiredJwtException e) {
            // Then
            fail("Unexpected exception");
        }
    }

    @Test
    void testGetExpirationDateFromExpiredToken() {
        assertThrows(ExpiredJwtException.class, () -> jwtTokenUtils.getExpirationDateFromToken(EXPIRED_TOKEN));
    }

    @Test
    void testGenerateToken() {
        // Given
        String username = "roger.federer@email.com";
        LocalDateTime now = LocalDateTime.now();

        // When
        String token = jwtTokenUtils.generateToken(username);

        // Then
        assertNotNull(token);
        assertTrue(jwtTokenUtils.isValidToken(token, username));
        assertNotNull(jwtTokenUtils.getExpirationDateFromToken(token));
        assertTrue(now.plusSeconds(tokenMaxTime * 60 + 1).isAfter(jwtTokenUtils.getExpirationDateFromToken(token)));
    }

    @Test
    void testIsValidTokenWithExpiredToken() {
        // Then
        assertThrows(ExpiredJwtException.class, () -> jwtTokenUtils.isValidToken(EXPIRED_TOKEN, "pete.sampras@email.com"));
    }
}
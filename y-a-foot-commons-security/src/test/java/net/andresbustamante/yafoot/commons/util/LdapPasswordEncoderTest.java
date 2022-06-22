package net.andresbustamante.yafoot.commons.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LdapPasswordEncoderTest {

    private LdapPasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        passwordEncoder = new LdapPasswordEncoder();
    }

    @Test
    void encode() {
        // When
        var result = passwordEncoder.encode("demodemo");

        // Then
        assertNotNull(result);
        assertTrue(result.startsWith("{BCRYPT}"));
    }

    @Test
    void matches() {
        // Given
        String password = "demodemo";

        // When
        var matches = passwordEncoder.matches("fjBE7dmxrRrD524u",
                "{BCRYPT}JDJhJDEwJEVOQ3R4Y3FYZzIycjFaT3ZmTnVwSi42blJmU2E0cHF4YlpmTkUvdkJyMTBoZEc0OUdkTXEy");

        // Then
        assertTrue(matches);
    }
}
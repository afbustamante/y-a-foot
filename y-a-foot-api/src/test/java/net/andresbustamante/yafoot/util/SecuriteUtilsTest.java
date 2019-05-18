package net.andresbustamante.yafoot.util;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import org.junit.Test;

import static org.junit.Assert.*;

public class SecuriteUtilsTest {

    @Test
    public void crypterMotDePasse() throws Exception {
        // Given
        String mdp = "demodemo";
        String digest = "WGPZ5MvfUi6qYuB0f86xxbJJuhM=";

        // When
        String resultat = SecuriteUtils.crypterMotDePasse(mdp);

        // Then
        assertEquals(digest, resultat);
    }

    @Test
    public void crypterMotDePasseAlgorithmeNonSupporte() throws Exception {
        // Given
        String mdp = "demodemo";
        String algo = "SHA1024";

        // When
        try {
            String resultat = SecuriteUtils.crypterMotDePasse(mdp, algo);
            fail();
        } catch (ApplicationException e) {
            // Then
            assertEquals("security.digest.format.not.supported", e.getMessage());
        }
    }

    @Test
    public void crypterMotDePasseMD5() throws Exception {
        String mdp = "demodemo";
        String algo = "MD5";
        String digest = "xRTJHk7TQfJj5FjUSzuwpw==";

        // When
        String resultat = SecuriteUtils.crypterMotDePasse(mdp, algo);
        assertNotNull(resultat);
        assertEquals(digest, resultat);
    }
}
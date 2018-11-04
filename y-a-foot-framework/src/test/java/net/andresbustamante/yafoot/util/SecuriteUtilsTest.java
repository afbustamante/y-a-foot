package net.andresbustamante.yafoot.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class SecuriteUtilsTest {

    @Test
    public void crypterMotDePasse() throws Exception {
        String mdp = "demodemo";
        String digest = "WGPZ5MvfUi6qYuB0f86xxbJJuhM=";

        assertEquals(digest, SecuriteUtils.crypterMotDePasse(mdp));
    }
}
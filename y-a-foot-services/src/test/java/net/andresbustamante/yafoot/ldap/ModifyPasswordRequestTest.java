package net.andresbustamante.yafoot.ldap;

import org.junit.jupiter.api.Test;

import javax.naming.CannotProceedException;
import javax.naming.SizeLimitExceededException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests pour la classe {@link ModifyPasswordRequest}
 */
class ModifyPasswordRequestTest {

    private static final String DN_TEST = "uid=test@email.com,ou=yafoot,ou=fr";
    private static final String MDP_TEST = "9(S/JSDHYYe238r1";

    @Test
    void testDnNul() {
        assertThrows(NullPointerException.class, () -> new ModifyPasswordRequest(null, MDP_TEST));
    }

    @Test
    void testDnVide() {
        assertThrows(CannotProceedException.class, () -> new ModifyPasswordRequest("", MDP_TEST));
    }

    @Test
    void testMotDePasseNul() {
        assertThrows(NullPointerException.class, () -> new ModifyPasswordRequest(DN_TEST, null));
    }

    @Test
    void testMotDePasseVide() {
        assertThrows(CannotProceedException.class, () -> new ModifyPasswordRequest(DN_TEST, ""));
    }

    @Test
    void testMotDePasseTropLong() {
        String mdp = "4C6F72656D20697073756D20646F6C6F722073697420616D65742C20636F6E7365637465747572206164697069736" +
                "3696E6720656C69742E205574206964206E756E63206C696265726F2E20457469616D207072657469756D2074757270697" +
                "320616320677261766964612064696374756D2E20496E746567657220657420756C747269636965732073617069656E2E2";
        assertThrows(SizeLimitExceededException.class, () -> new ModifyPasswordRequest(DN_TEST, mdp));
    }

    @Test
    void testDnEtMotDePasseTropLongs() {
        String dn = "uid=untrestreslongemail@monsousdomain.untreslongdomain.fr,ou=yafoot,ou=fr";
        String mdp = "4C6F72656D20697073756D20646F6C6F722073697420616D65742C20636F6E7365637465747572206164697069736" +
                "3696E6720656C69742E205574206964206E756E63206C696265726F2E20457469616D207072657469756D2074757270697";
        assertThrows(SizeLimitExceededException.class, () -> new ModifyPasswordRequest(dn, mdp));
    }
}

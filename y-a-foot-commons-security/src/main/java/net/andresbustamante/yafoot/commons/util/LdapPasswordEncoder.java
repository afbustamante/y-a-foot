package net.andresbustamante.yafoot.commons.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Password encoder used to store the encoded passwords in the LDAP directory.
 */
public final class LdapPasswordEncoder implements PasswordEncoder {

    /**
     * Prefix to use for every password to store in the LDAP tree.
     */
    private static final String PWD_PREFIX = "{BCRYPT}";

    /**
     * Password encoder to use.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Default constructor tu use BCRYPT encoding.
     */
    public LdapPasswordEncoder() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public String encode(CharSequence rawPassword) {
        // Prefix so that Apache directory understands that bcrypt has been used.
        // Without this, it assumes SSHA and fails during authentication.
        String cryptPassword = passwordEncoder.encode(rawPassword);
        return PWD_PREFIX + Utf8.decode(Base64.getEncoder().encode(cryptPassword.getBytes(StandardCharsets.UTF_8)));
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // remove {BCRYPT} prefix
        String cryptPassword = Utf8.decode(Base64.getDecoder().decode(encodedPassword.substring(PWD_PREFIX.length())));
        return passwordEncoder.matches(rawPassword, cryptPassword);
    }
}

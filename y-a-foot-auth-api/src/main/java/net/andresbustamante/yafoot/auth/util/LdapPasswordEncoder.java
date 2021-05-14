package net.andresbustamante.yafoot.auth.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Password encoder used to store the encoded passwords in the LDAP directory
 */
public class LdapPasswordEncoder implements PasswordEncoder {

    private static final String PWD_PREFIX = "{BCRYPT}";

    private PasswordEncoder passwordEncoder;

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

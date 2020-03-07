package net.andresbustamante.yafoot.ldap;

import net.andresbustamante.yafoot.model.User;
import org.springframework.ldap.core.AttributesMapper;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import static net.andresbustamante.yafoot.util.LdapConstants.*;

/**
 * LDAP authentication attributes mapper for {@link User}
 */
public class LdapAuthUserMapper implements AttributesMapper<User> {

    @Override
    public User mapFromAttributes(Attributes attrs) throws NamingException {
        User user = new User();
        user.setSurname((String) attrs.get(SN).get());
        user.setFirstName((String) attrs.get(GIVEN_NAME).get());
        user.setEmail((String) attrs.get(MAIL).get());

        byte[] password = (byte[]) attrs.get(USER_PASSWORD).get();

        user.setPassword(new String(password));
        return user;
    }

}

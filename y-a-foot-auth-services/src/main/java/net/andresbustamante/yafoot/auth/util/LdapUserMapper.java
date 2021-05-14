package net.andresbustamante.yafoot.auth.util;

import net.andresbustamante.yafoot.commons.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.stereotype.Component;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;

import static net.andresbustamante.yafoot.auth.util.LdapConstants.*;

/**
 * LDAP attributes mapper for {@link User}
 */
@Component
public class LdapUserMapper implements AttributesMapper<User> {

    private static final String[] USER_CLASSES = {"top", "person", "organizationalPerson", "inetOrgPerson"};

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

    /**
     * Build LDAP attributes from an user passed as a parameter
     *
     * @param usr User to process
     * @return
     */
    public Attributes mapToAttributes(User usr) {
        BasicAttribute objectClass = new BasicAttribute(OBJECT_CLASS);
        for (String class_ : USER_CLASSES) {
            objectClass.add(class_);
        }

        Attributes attrs = new BasicAttributes();
        attrs.put(objectClass);
        attrs.put(UID, usr.getEmail());
        attrs.put(MAIL, usr.getEmail());

        if (usr.getSurname() != null) {
            attrs.put(SN, usr.getSurname());
        }

        if (usr.getFirstName() != null) {
            attrs.put(CN, usr.getFirstName());
            attrs.put(GIVEN_NAME, usr.getFirstName());
        }

        if (usr.getFirstName() != null && usr.getSurname() != null) {
            attrs.put(DISPLAY_NAME, StringUtils.joinWith(" ",usr.getFirstName(), usr.getSurname()));
        }

        if (usr.getPassword() != null) {
            attrs.put(USER_PASSWORD, usr.getPassword());
        }
        return attrs;
    }
}

package net.andresbustamante.yafoot.ldap;

import net.andresbustamante.yafoot.model.Utilisateur;
import org.springframework.ldap.core.AttributesMapper;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;

import static net.andresbustamante.yafoot.util.LdapConstants.*;

/**
 * LDAP attributes mapper for {@link Utilisateur}
 */
public class UtilisateurMapper implements AttributesMapper<Utilisateur> {

    @Override
    public Utilisateur mapFromAttributes(Attributes attrs) throws NamingException {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom((String) attrs.get(SN).get());
        utilisateur.setPrenom((String) attrs.get(GIVEN_NAME).get());
        utilisateur.setEmail((String) attrs.get(MAIL).get());
        return utilisateur;
    }

    /**
     * Build LDAP attributes from an user passed as a parameter
     *
     * @param usr User to process
     * @return
     */
    public Attributes mapToAttributes(Utilisateur usr) {
        BasicAttribute objectClass = new BasicAttribute(OBJECT_CLASS);
        objectClass.add("top");
        objectClass.add("person");
        objectClass.add("organizationalPerson");
        objectClass.add("inetOrgPerson");

        Attributes attrs = new BasicAttributes();
        attrs.put(objectClass);
        attrs.put(UID, usr.getEmail());
        attrs.put(MAIL, usr.getEmail());

        if (usr.getNom() != null) {
            attrs.put(SN, usr.getNom());
        }

        if (usr.getPrenom() != null) {
            attrs.put(CN, usr.getPrenom());
            attrs.put(GIVEN_NAME, usr.getPrenom());
        }

        if (usr.getPrenom() != null && usr.getNom() != null) {
            attrs.put(DISPLAY_NAME, usr.getPrenom() + " " + usr.getNom());
        }
        return attrs;
    }
}

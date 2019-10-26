package net.andresbustamante.yafoot.ldap;

import net.andresbustamante.yafoot.model.Utilisateur;
import org.springframework.ldap.core.AttributesMapper;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import static net.andresbustamante.yafoot.util.LdapConstants.GIVEN_NAME;
import static net.andresbustamante.yafoot.util.LdapConstants.MAIL;
import static net.andresbustamante.yafoot.util.LdapConstants.SN;

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
}

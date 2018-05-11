package net.andresbustamante.yafoot.ldap.impl;

import net.andresbustamante.yafoot.ldap.UtilisateurDAO;
import net.andresbustamante.yafoot.model.Utilisateur;
import net.andresbustamante.yafoot.model.enums.RolesEnum;
import net.andresbustamante.yafoot.util.ConstantesLdapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Repository;

import javax.naming.Name;
import javax.naming.directory.*;

@Repository
public class UtilisateurDAOImpl implements UtilisateurDAO {

    @Autowired
    private LdapTemplate ldapTemplate;

    @Value("${ldap.users.dn}")
    private String dnUtilisateurs;

    @Value("${ldap.roles.dn}")
    private String dnRoles;

    @Override
    public void creerUtilisateur(Utilisateur usr, RolesEnum role) {
        ldapTemplate.bind(getIdAnnuaire(usr), null, getAttributesLdap(usr));
        affecterRole(usr, role);
    }

    @Override
    public void actualiserUtilisateur(Utilisateur usr) {
        ldapTemplate.rebind(getIdAnnuaire(usr), null, getAttributesLdap(usr));
    }

    /**
     * @param usr
     * @return
     */
    private Name getIdAnnuaire(Utilisateur usr) {
        return LdapNameBuilder.newInstance(dnUtilisateurs).add(ConstantesLdapUtils.UID, usr.getEmail()).build();
    }

    /**
     *
     * @param role
     * @return
     */
    private Name getIdAnnuaire(RolesEnum role) {
        return LdapNameBuilder.newInstance(dnRoles).add(ConstantesLdapUtils.CN, role.name()).build();
    }

    /**
     * @param usr
     * @return
     */
    private Attributes getAttributesLdap(Utilisateur usr) {
        BasicAttribute objectClass = new BasicAttribute("objectclass");
        objectClass.add("top");
        objectClass.add("person");
        objectClass.add("organizationalPerson");
        objectClass.add("inetOrgPerson");

        Attributes attrs = new BasicAttributes();
        attrs.put(objectClass);
        attrs.put(ConstantesLdapUtils.UID, usr.getEmail());
        attrs.put("mail", usr.getEmail());

        if (usr.getNom() != null) {
            attrs.put("sn", usr.getNom());
        }

        if (usr.getPrenom() != null) {
            attrs.put(ConstantesLdapUtils.CN, usr.getPrenom());
            attrs.put("givenName", usr.getPrenom());
        }

        attrs.put("userPassword", ConstantesLdapUtils.PREFIX_MDP + usr.getMotDePasse());

        if (usr.getPrenom() != null && usr.getNom() != null) {
            attrs.put("displayName", usr.getPrenom() + " " + usr.getNom());
        }
        return attrs;
    }

    /**
     *
     * @param usr
     * @param role
     */
    private void affecterRole(Utilisateur usr, RolesEnum role) {
        Attribute attribute = new BasicAttribute("member");
        attribute.add(getIdAnnuaire(usr).toString());
        ModificationItem item = new ModificationItem(DirContext.ADD_ATTRIBUTE, attribute);
        ldapTemplate.modifyAttributes(getIdAnnuaire(role), new ModificationItem[]{item});
    }
}

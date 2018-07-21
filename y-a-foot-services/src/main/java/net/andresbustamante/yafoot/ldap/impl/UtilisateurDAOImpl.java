package net.andresbustamante.yafoot.ldap.impl;

import net.andresbustamante.yafoot.ldap.UtilisateurDAO;
import net.andresbustamante.yafoot.model.Utilisateur;
import net.andresbustamante.yafoot.model.enums.RolesEnum;
import net.andresbustamante.yafoot.util.ConstantesLdapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.NameNotFoundException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Repository;

import javax.naming.Name;
import javax.naming.NamingException;
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

    @Override
    public void supprimerUtilisateur(Utilisateur usr, RolesEnum[] roles) {
        desaffecterRoles(usr, roles);
        ldapTemplate.unbind(getIdAnnuaire(usr));
    }

    @Override
    public Utilisateur chercherUtilisateur(String uid) {
        try {
            return (Utilisateur) ldapTemplate.lookup(uid, new AttributesUtilisateurMapper());
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    /**
     * Obtenir l'identifiant LDAP pour un utilisateur passé en paramètre
     *
     * @param usr Utilisateur à processer
     * @return
     */
    private Name getIdAnnuaire(Utilisateur usr) {
        return LdapNameBuilder.newInstance(dnUtilisateurs).add(ConstantesLdapUtils.UID, usr.getEmail()).build();
    }

    /**
     * Obtenir l'identifiant LDAP pour un rôle passé en paramètre
     *
     * @param role Rôle à processer
     * @return
     */
    private Name getIdAnnuaire(RolesEnum role) {
        return LdapNameBuilder.newInstance(dnRoles).add(ConstantesLdapUtils.CN, role.name()).build();
    }

    /**
     * Construire des attributes LDAP à partir des information d'un utilisateur passé en paramètre
     *
     * @param usr Utilisateur à processer
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
     * Affecter un rôle pour un utilisateur passé en paramètre
     *
     * @param usr  Utilisateur à impacter
     * @param role Rôle à affecter
     */
    private void affecterRole(Utilisateur usr, RolesEnum role) {
        Attribute attribute = new BasicAttribute("member");
        attribute.add(getIdAnnuaire(usr).toString());
        ModificationItem item = new ModificationItem(DirContext.ADD_ATTRIBUTE, attribute);
        ldapTemplate.modifyAttributes(getIdAnnuaire(role), new ModificationItem[]{item});
    }

    /**
     * Désaffecter les rôles existants pour un utilisateur
     *
     * @param usr Utilisateur à impacter
     */
    private void desaffecterRoles(Utilisateur usr, RolesEnum[] roles) {
        for (RolesEnum role : roles) {
            Attribute attribute = new BasicAttribute("member");
            attribute.add(getIdAnnuaire(usr).toString());
            ModificationItem item = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, attribute);
            ldapTemplate.modifyAttributes(getIdAnnuaire(role), new ModificationItem[]{item});
        }
    }

    /**
     * Mapper des attributes LDAP vers des objets de type Utilisateur
     */
    private class AttributesUtilisateurMapper implements AttributesMapper {
        public Object mapFromAttributes(Attributes attrs) throws NamingException {
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setNom((String) attrs.get("sn").get());
            utilisateur.setPrenom((String) attrs.get("givenName").get());
            utilisateur.setEmail((String) attrs.get("mail").get());
            return utilisateur;
        }
    }
}

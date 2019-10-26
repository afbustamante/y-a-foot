package net.andresbustamante.yafoot.ldap.impl;

import net.andresbustamante.yafoot.ldap.ModifyPasswordRequest;
import net.andresbustamante.yafoot.ldap.UtilisateurMapper;
import net.andresbustamante.yafoot.ldap.UtilisateurDAO;
import net.andresbustamante.yafoot.model.Utilisateur;
import net.andresbustamante.yafoot.model.enums.RolesEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.NameNotFoundException;
import org.springframework.ldap.core.ContextExecutor;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Repository;

import javax.naming.Name;
import javax.naming.directory.*;
import javax.naming.ldap.ExtendedRequest;
import javax.naming.ldap.LdapContext;

import static net.andresbustamante.yafoot.util.LdapConstants.*;

@Repository
public class UtilisateurDAOImpl implements UtilisateurDAO {

    @Autowired
    private LdapTemplate ldapTemplate;

    @Value("${ldap.config.users.dn}")
    private String dnUtilisateurs;

    @Value("${ldap.config.roles.dn}")
    private String dnRoles;

    private UtilisateurMapper utilisateurMapper;

    public UtilisateurDAOImpl() {
        utilisateurMapper = new UtilisateurMapper();
    }

    @Override
    public void creerUtilisateur(Utilisateur usr, RolesEnum role) {
        ldapTemplate.bind(getIdAnnuaire(usr), null, utilisateurMapper.mapToAttributes(usr));
        modifierMotDePasse(usr);
        affecterRole(usr, role);
    }

    @Override
    public void actualiserUtilisateur(Utilisateur usr) {
        if (usr.getMotDePasse() != null) {
            modifierMotDePasse(usr);
        } else {
            ldapTemplate.rebind(getIdAnnuaire(usr), null, utilisateurMapper.mapToAttributes(usr));
        }
    }

    @Override
    public void supprimerUtilisateur(Utilisateur usr, RolesEnum[] roles) {
        desaffecterRoles(usr, roles);
        ldapTemplate.unbind(getIdAnnuaire(usr));
    }

    @Override
    public Utilisateur chercherUtilisateur(String uid) {
        try {
            return ldapTemplate.lookup(uid, utilisateurMapper);
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
        return LdapNameBuilder.newInstance(dnUtilisateurs).add(UID, usr.getEmail()).build();
    }

    /**
     * Obtenir l'identifiant LDAP pour un rôle passé en paramètre
     *
     * @param role Rôle à processer
     * @return
     */
    private Name getIdAnnuaire(RolesEnum role) {
        return LdapNameBuilder.newInstance(dnRoles).add(CN, role.name()).build();
    }

    /**
     * Affecter un rôle pour un utilisateur passé en paramètre
     *
     * @param usr  Utilisateur à impacter
     * @param role Rôle à affecter
     */
    private void affecterRole(Utilisateur usr, RolesEnum role) {
        Attribute attribute = new BasicAttribute(MEMBER);
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
            Attribute attribute = new BasicAttribute(MEMBER);
            attribute.add(getIdAnnuaire(usr).toString());
            ModificationItem item = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, attribute);
            ldapTemplate.modifyAttributes(getIdAnnuaire(role), new ModificationItem[]{item});
        }
    }

    /**
     * Modifier le mot de passe d'un utilisateur présent dans l'annuaire LDAP
     *
     * @param usr Utilisateur avec le nouveau mot de passe
     */
    private void modifierMotDePasse(Utilisateur usr) {
        String dn = getIdAnnuaire(usr).toString();

        ldapTemplate.executeReadWrite((ContextExecutor<Object>) ctx -> {
            if (!(ctx instanceof LdapContext)) {
                throw new IllegalArgumentException(
                        "Extended operations require LDAPv3 - "
                                + "Context must be of type LdapContext");
            }
            LdapContext ldapContext = (LdapContext) ctx;
            ExtendedRequest er = new ModifyPasswordRequest(dn, usr.getMotDePasse());
            return ldapContext.extendedOperation(er);
        });
    }
}

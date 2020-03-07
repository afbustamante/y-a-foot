package net.andresbustamante.yafoot.ldap.impl;

import net.andresbustamante.yafoot.ldap.LdapAuthUserMapper;
import net.andresbustamante.yafoot.ldap.ModifyPasswordRequest;
import net.andresbustamante.yafoot.ldap.LdapUserMapper;
import net.andresbustamante.yafoot.ldap.UserDAO;
import net.andresbustamante.yafoot.model.User;
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
public class UserDAOImpl implements UserDAO {

    @Autowired
    private LdapTemplate ldapTemplate;

    @Value("${ldap.config.users.dn}")
    private String usersDn;

    @Value("${ldap.config.roles.dn}")
    private String rolesDn;

    private LdapUserMapper ldapUserMapper;

    private LdapAuthUserMapper ldapAuthUserMapper;

    public UserDAOImpl() {
        ldapUserMapper = new LdapUserMapper();
        ldapAuthUserMapper = new LdapAuthUserMapper();
    }

    @Override
    public void saveUser(User usr, RolesEnum role) {
        ldapTemplate.bind(getUid(usr), null, ldapUserMapper.mapToAttributes(usr));
        modifyPassword(usr);
        addRoleForUser(usr, role);
    }

    @Override
    public void updateUser(User usr) {
        if (usr.getPassword() != null) {
            modifyPassword(usr);
        } else {
            ldapTemplate.rebind(getUid(usr), null, ldapUserMapper.mapToAttributes(usr));
        }
    }

    @Override
    public void deleteUser(User usr, RolesEnum[] roles) {
        removeRoleForUser(usr, roles);
        ldapTemplate.unbind(getUid(usr));
    }

    @Override
    public User findUserByUid(String uid) {
        try {
            return ldapTemplate.lookup(uid, ldapUserMapper);
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    @Override
    public User findUserAuthDetailsByUid(String uid) {
        try {
            return ldapTemplate.lookup(getUid(new User(uid)), ldapAuthUserMapper);
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    @Override
    public boolean authenticateUser(String uid, String password) {
        try {
            return ldapTemplate.authenticate(usersDn, "(uid=" + uid + ")", password);
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    /**
     * Obtenir l'identifiant LDAP pour un utilisateur passé en paramètre
     *
     * @param usr User à processer
     * @return
     */
    private Name getUid(User usr) {
        return LdapNameBuilder.newInstance(usersDn).add(UID, usr.getEmail()).build();
    }

    /**
     * Obtenir l'identifiant LDAP pour un rôle passé en paramètre
     *
     * @param role Rôle à processer
     * @return
     */
    private Name getCn(RolesEnum role) {
        return LdapNameBuilder.newInstance(rolesDn).add(CN, role.name()).build();
    }

    /**
     * Affecter un rôle pour un utilisateur passé en paramètre
     *
     * @param usr  User à impacter
     * @param role Rôle à affecter
     */
    private void addRoleForUser(User usr, RolesEnum role) {
        Attribute attribute = new BasicAttribute(MEMBER);
        attribute.add(getUid(usr).toString());
        ModificationItem item = new ModificationItem(DirContext.ADD_ATTRIBUTE, attribute);
        ldapTemplate.modifyAttributes(getCn(role), new ModificationItem[]{item});
    }

    /**
     * Désaffecter les rôles existants pour un utilisateur
     *
     * @param usr User à impacter
     */
    private void removeRoleForUser(User usr, RolesEnum[] roles) {
        for (RolesEnum role : roles) {
            Attribute attribute = new BasicAttribute(MEMBER);
            attribute.add(getUid(usr).toString());
            ModificationItem item = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, attribute);
            ldapTemplate.modifyAttributes(getCn(role), new ModificationItem[]{item});
        }
    }

    /**
     * Modifier le mot de passe d'un utilisateur présent dans l'annuaire LDAP
     *
     * @param usr User avec le nouveau mot de passe
     */
    private void modifyPassword(User usr) {
        String dn = getUid(usr).toString();

        ldapTemplate.executeReadWrite((ContextExecutor<Object>) ctx -> {
            if (!(ctx instanceof LdapContext)) {
                throw new IllegalArgumentException(
                        "Extended operations require LDAPv3 - "
                                + "Context must be of type LdapContext");
            }
            LdapContext ldapContext = (LdapContext) ctx;
            ExtendedRequest er = new ModifyPasswordRequest(dn, usr.getPassword());
            return ldapContext.extendedOperation(er);
        });
    }
}

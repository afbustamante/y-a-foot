package net.andresbustamante.yafoot.auth.repository.impl;

import net.andresbustamante.yafoot.commons.model.enums.RolesEnum;
import net.andresbustamante.yafoot.users.repository.UserRepository;
import net.andresbustamante.yafoot.commons.util.LdapPasswordEncoder;
import net.andresbustamante.yafoot.auth.util.LdapUserMapper;
import net.andresbustamante.yafoot.users.model.User;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.NameNotFoundException;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Repository;

import javax.naming.Name;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import java.util.Collections;
import java.util.List;

import static net.andresbustamante.yafoot.auth.util.LdapConstants.*;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private LdapTemplate ldapTemplate;

    @Value("${app.ldap.config.users.dn}")
    private String usersDn;

    @Value("${app.ldap.config.roles.dn}")
    private String rolesDn;

    private LdapUserMapper ldapUserMapper;

    private LdapPasswordEncoder passwordEncoder;

    @Autowired
    public UserRepositoryImpl(LdapTemplate ldapTemplate, LdapUserMapper ldapUserMapper) {
        this.ldapTemplate = ldapTemplate;
        this.ldapUserMapper = ldapUserMapper;
        this.passwordEncoder = new LdapPasswordEncoder();
    }

    @Override
    public void saveUser(User usr, RolesEnum role) {
        ldapTemplate.bind(getUid(usr), null, ldapUserMapper.mapToAttributes(usr));
        modifyPassword(usr);
        addRoleForUser(usr, role);
    }

    @Override
    public void updateUser(User usr) {
        User oldUser = ldapTemplate.lookup(getUid(usr).toString(), ldapUserMapper);
        usr.setPassword(oldUser.getPassword());
        ldapTemplate.rebind(getUid(usr), null, ldapUserMapper.mapToAttributes(usr));
    }

    @Override
    public void updatePassword(User usr) {
        modifyPassword(usr);
    }

    @Override
    public void deleteUser(User usr) {
        List<RolesEnum> userRoles = Collections.singletonList(RolesEnum.PLAYER); // TODO Calculate roles for the given user
        removeRolesForUser(usr, userRoles);
        ldapTemplate.unbind(getUid(usr));
    }

    @Override
    public User findUserByEmail(String email) {
        User user = new User(email);
        return findUserByUid(getUid(user).toString());
    }

    @Override
    public User authenticateUser(String uid, String password) {
        User user = new User(uid);
        user = findUserByUid(getUid(user).toString());

        return (user != null && user.getPassword() != null && passwordEncoder.matches(password, user.getPassword())) ? user : null;
    }

    @Override
    public User findUserByToken(String token) {
        List<User> userList = ldapTemplate.search(usersDn, "(description=" + token + ")", ldapUserMapper);
        return (CollectionUtils.isNotEmpty(userList)) ? userList.iterator().next() : null;
    }

    @Override
    public void saveTokenForUser(String token, User user) {
        Attribute attribute = new BasicAttribute(TOKEN);
        attribute.add(token);
        ModificationItem item = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attribute);
        ldapTemplate.modifyAttributes(getUid(user), new ModificationItem[]{item});
    }

    @Override
    public void removeTokenForUser(User user) {
        Attribute attribute = new BasicAttribute(TOKEN);
        ModificationItem item = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, attribute);
        ldapTemplate.modifyAttributes(getUid(user), new ModificationItem[]{item});
    }

    private User findUserByUid(String uid) {
        try {
            return ldapTemplate.lookup(uid, ldapUserMapper);
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    /**
     * Gets the LDAP identifier for a given user
     *
     * @param usr User to check
     * @return LDAP name for the given user
     */
    private Name getUid(User usr) {
        return LdapNameBuilder.newInstance(usersDn).add(UID, usr.getEmail()).build();
    }

    /**
     * Gets the LDAP identifier for a given role
     *
     * @param role Role to check
     * @return LDAP name for the given role
     */
    private Name getCn(RolesEnum role) {
        return LdapNameBuilder.newInstance(rolesDn).add(CN, role.name()).build();
    }

    /**
     * Adds a role for a given user
     *
     * @param usr  User to update
     * @param role Role to give to the user
     */
    private void addRoleForUser(User usr, RolesEnum role) {
        Attribute attribute = new BasicAttribute(MEMBER);
        attribute.add(getUid(usr).toString());
        ModificationItem item = new ModificationItem(DirContext.ADD_ATTRIBUTE, attribute);
        ldapTemplate.modifyAttributes(getCn(role), new ModificationItem[]{item});
    }

    /**
     * Remove a list of roles for a given user
     *
     * @param usr User to check
     */
    private void removeRolesForUser(User usr, List<RolesEnum> roles) {
        for (RolesEnum role : roles) {
            Attribute attribute = new BasicAttribute(MEMBER);
            attribute.add(getUid(usr).toString());
            ModificationItem item = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, attribute);
            ldapTemplate.modifyAttributes(getCn(role), new ModificationItem[]{item});
        }
    }

    /**
     * Update a user's password in LDAP directory
     *
     * @param usr User containing his/her new password
     */
    private void modifyPassword(User usr) {
        String dn = getUid(usr).toString();
        String passwd = passwordEncoder.encode(usr.getPassword());

        Attribute attribute = new BasicAttribute(USER_PASSWORD);
        attribute.add(passwd);
        ModificationItem item = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attribute);
        ldapTemplate.modifyAttributes(dn, new ModificationItem[]{item});
    }
}

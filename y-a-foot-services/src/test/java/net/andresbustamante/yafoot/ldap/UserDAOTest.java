package net.andresbustamante.yafoot.ldap;

import net.andresbustamante.yafoot.config.LdapConfig;
import net.andresbustamante.yafoot.config.LdapTestConfig;
import net.andresbustamante.yafoot.model.User;
import net.andresbustamante.yafoot.model.enums.RolesEnum;
import net.andresbustamante.yafoot.util.LdapConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.naming.Name;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author andresbustamante
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {LdapTestConfig.class, LdapConfig.class})
class UserDAOTest {

    private static final User USR_TEST = new User("test@email.com", "password", "TEST", "User");

    @Value("${ldap.config.users.dn}")
    private String dnUtilisateurs;

    @Autowired
    private UserDAO userDAO;

    @BeforeEach
    void setUp() throws Exception {
        userDAO.saveUser(USR_TEST, RolesEnum.PLAYER);
    }

    @AfterEach
    void tearDown() throws Exception {
        userDAO.deleteUser(USR_TEST, new RolesEnum[]{RolesEnum.PLAYER});
    }

    @Test
    void saveUser() throws Exception {
        User newUser = buildNewUser();

        userDAO.saveUser(newUser, RolesEnum.PLAYER);

        User user = userDAO.findUserByUid(getUid(newUser).toString());
        assertNotNull(user);
        assertNotNull(user.getSurname());
        assertNotNull(user.getFirstName());
        assertNotNull(user.getEmail());
        assertNull(user.getPassword());

        userDAO.deleteUser(newUser, new RolesEnum[]{RolesEnum.PLAYER});
    }

    @Test
    void updateUser() throws Exception {
        User updatedUser = new User();
        updatedUser.setEmail(USR_TEST.getEmail());
        updatedUser.setSurname(USR_TEST.getSurname() + " autre");
        updatedUser.setFirstName(USR_TEST.getFirstName() + " autre");

        userDAO.updateUser(updatedUser);

        User user = userDAO.findUserByUid(getUid(updatedUser).toString());
        assertNotNull(user);
        assertEquals(USR_TEST.getEmail(), user.getEmail());
        assertNotNull(user.getSurname());
        assertEquals(USR_TEST.getSurname() + " autre", user.getSurname());
        assertNotNull(user.getFirstName());
        assertEquals(USR_TEST.getFirstName() + " autre", user.getFirstName());
        assertNotNull(user.getSurname());

        // Remettre les informations de l'user
        user.setSurname(USR_TEST.getSurname());
        user.setFirstName(USR_TEST.getFirstName());
        userDAO.updateUser(user);
    }

    @Test
    void updateUserPassword() throws Exception {
        // Given
        User updatedUser = new User();
        updatedUser.setEmail(USR_TEST.getEmail());
        updatedUser.setSurname(USR_TEST.getSurname() + " autre");
        updatedUser.setFirstName(USR_TEST.getFirstName() + " autre");
        updatedUser.setPassword("monNouveauMotDePasse");

        // When
        userDAO.updateUser(updatedUser);
        User user = userDAO.findUserByUid(getUid(updatedUser).toString());

        // Then
        assertNotNull(user);
        assertEquals(USR_TEST.getSurname(), user.getSurname()); // Non modifié
        assertEquals(USR_TEST.getFirstName(), user.getFirstName()); // Non modifié
        assertNull(user.getPassword()); // Non chargé pour sécurité

        // Remettre les informations de l'user
        user.setSurname(USR_TEST.getSurname());
        user.setFirstName(USR_TEST.getFirstName());
        userDAO.updateUser(user);
    }

    @Test
    void deleteUser() throws Exception {
        User newUser = buildNewUser();
        userDAO.saveUser(newUser, RolesEnum.PLAYER);

        userDAO.deleteUser(newUser, new RolesEnum[]{RolesEnum.PLAYER});

        User user = userDAO.findUserByUid(getUid(newUser).toString());
        assertNull(user);
    }

    @Test
    void findUser() throws Exception {
        User user = userDAO.findUserByUid(getUid(USR_TEST).toString());
        assertNotNull(user);
        assertNotNull(user.getEmail());
        assertEquals(USR_TEST.getEmail(), user.getEmail());
        assertNotNull(user.getSurname());
        assertEquals(USR_TEST.getSurname(), user.getSurname());
        assertNotNull(user.getFirstName());
        assertEquals(USR_TEST.getFirstName(), user.getFirstName());
        assertNotNull(user.getSurname());
        assertNull(user.getPassword());
    }

    @Test
    void authenticateValidUser() {
        User authenticatedUser = userDAO.authenticateUser("test@email.com", "password");
        assertNotNull(authenticatedUser);
        assertNotNull(authenticatedUser.getEmail());
        assertEquals("test@email.com", authenticatedUser.getEmail());
        assertNotNull(authenticatedUser.getFirstName());
        assertEquals("User", authenticatedUser.getFirstName());
        assertNotNull(authenticatedUser.getSurname());
        assertEquals("TEST", authenticatedUser.getSurname());
    }

    @Test
    void authenticateInvalidUsername() {
        User authenticatedUser = userDAO.authenticateUser("unknown@email.com", "otherPassword");
        assertNull(authenticatedUser);
    }

    @Test
    void authenticateInvalidPassword() {
        User authenticatedUser = userDAO.authenticateUser("test@email.com", "otherPassword");
        assertNull(authenticatedUser);
    }

    private User buildNewUser() {
        User user = new User();
        user.setEmail("nouvel.user@email.com");
        user.setPassword("password");
        user.setFirstName("User");
        user.setSurname("NOUVEL");
        return user;
    }

    private Name getUid(User usr) {
        return LdapNameBuilder.newInstance(dnUtilisateurs).add(LdapConstants.UID, usr.getEmail()).build();
    }
}
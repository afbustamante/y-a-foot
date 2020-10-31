package net.andresbustamante.yafoot.ldap;

import net.andresbustamante.yafoot.config.LdapConfig;
import net.andresbustamante.yafoot.config.LdapTestConfig;
import net.andresbustamante.yafoot.model.User;
import net.andresbustamante.yafoot.model.enums.RolesEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link UserRepository}
 *
 * @author andresbustamante
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {LdapTestConfig.class, LdapConfig.class})
class UserRepositoryTest {

    private static final User USR_TEST = new User("test@email.com", "password", "TEST", "User");

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() throws Exception {
        userRepository.saveUser(USR_TEST, RolesEnum.PLAYER);
    }

    @AfterEach
    void tearDown() throws Exception {
        userRepository.deleteUser(USR_TEST);
    }

    @Test
    void saveUser() throws Exception {
        User newUser = buildNewUser();

        userRepository.saveUser(newUser, RolesEnum.PLAYER);

        User user = userRepository.findUserByEmail(newUser.getEmail());
        assertNotNull(user);
        assertNotNull(user.getSurname());
        assertNotNull(user.getFirstName());
        assertNotNull(user.getEmail());
        assertNotNull(user.getPassword());

        userRepository.deleteUser(newUser);
    }

    @Test
    void updateUser() throws Exception {
        // Given
        User updatedUser = new User();
        updatedUser.setEmail(USR_TEST.getEmail());
        updatedUser.setSurname(USR_TEST.getSurname() + " autre");
        updatedUser.setFirstName(USR_TEST.getFirstName() + " autre");

        // When
        userRepository.updateUser(updatedUser);

        // Then
        User user = userRepository.authenticateUser(USR_TEST.getEmail(), "password");
        assertNotNull(user);
        assertEquals(USR_TEST.getEmail(), user.getEmail());
        assertNotNull(user.getSurname());
        assertEquals(USR_TEST.getSurname() + " autre", user.getSurname());
        assertNotNull(user.getFirstName());
        assertEquals(USR_TEST.getFirstName() + " autre", user.getFirstName());
        assertNotNull(user.getSurname());

        user.setSurname(USR_TEST.getSurname());
        user.setFirstName(USR_TEST.getFirstName());
        userRepository.updateUser(user);
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
        userRepository.updatePassword(updatedUser);
        User user = userRepository.findUserByEmail(updatedUser.getEmail());

        // Then
        assertNotNull(user);
        assertEquals(USR_TEST.getSurname(), user.getSurname()); // Unmodified
        assertEquals(USR_TEST.getFirstName(), user.getFirstName()); // Unmodified
        assertNotNull(user.getPassword());

        user.setSurname(USR_TEST.getSurname());
        user.setFirstName(USR_TEST.getFirstName());
        userRepository.updateUser(user);
    }

    @Test
    void deleteUser() throws Exception {
        User newUser = buildNewUser();
        userRepository.saveUser(newUser, RolesEnum.PLAYER);

        userRepository.deleteUser(newUser);

        User user = userRepository.findUserByEmail(newUser.getEmail());
        assertNull(user);
    }

    @Test
    void findUser() throws Exception {
        User user = userRepository.findUserByEmail(USR_TEST.getEmail());
        assertNotNull(user);
        assertNotNull(user.getEmail());
        assertEquals(USR_TEST.getEmail(), user.getEmail());
        assertNotNull(user.getSurname());
        assertEquals(USR_TEST.getSurname(), user.getSurname());
        assertNotNull(user.getFirstName());
        assertEquals(USR_TEST.getFirstName(), user.getFirstName());
        assertNotNull(user.getSurname());
        assertNotNull(user.getPassword());
    }

    @Test
    void authenticateValidUser() {
        User authenticatedUser = userRepository.authenticateUser("test@email.com", "password");
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
        User authenticatedUser = userRepository.authenticateUser("unknown@email.com", "otherPassword");
        assertNull(authenticatedUser);
    }

    @Test
    void authenticateInvalidPassword() {
        User authenticatedUser = userRepository.authenticateUser("test@email.com", "otherPassword");
        assertNull(authenticatedUser);
    }

    @Test
    void saveToken() {
        // Given
        String token = "token";

        // When
        userRepository.saveTokenForUser(token, USR_TEST);
        User user = userRepository.findUserByToken(token);

        // Then
        assertNotNull(user);
        assertEquals(USR_TEST.getEmail(), user.getEmail());
    }

    private User buildNewUser() {
        User user = new User();
        user.setEmail("nouvel.user@email.com");
        user.setPassword("password");
        user.setFirstName("User");
        user.setSurname("NEW");
        return user;
    }
}
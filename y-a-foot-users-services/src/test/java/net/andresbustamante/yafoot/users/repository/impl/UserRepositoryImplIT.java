package net.andresbustamante.yafoot.users.repository.impl;

import net.andresbustamante.yafoot.users.config.LdapConfig;
import net.andresbustamante.yafoot.users.config.LdapTestConfig;
import net.andresbustamante.yafoot.users.model.User;
import net.andresbustamante.yafoot.users.model.enums.RolesEnum;
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
 * Integration tests for {@link UserRepositoryImpl}.
 *
 * @author andresbustamante
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {LdapTestConfig.class, LdapConfig.class})
class UserRepositoryImplIT {

    private static final User USR_TEST = new User("test@email.com", "password", "TEST", "User");

    @Autowired
    private UserRepositoryImpl userRepository;

    @BeforeEach
    void setUp() throws Exception {
        userRepository.saveUser(USR_TEST, RolesEnum.PLAYER);
    }

    @AfterEach
    void tearDown() throws Exception {
        userRepository.deleteUser(USR_TEST);
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

    private User buildNewUser() {
        User user = new User();
        user.setEmail("nouvel.user@email.com");
        user.setPassword("password");
        user.setFirstName("User");
        user.setSurname("NEW");
        return user;
    }
}

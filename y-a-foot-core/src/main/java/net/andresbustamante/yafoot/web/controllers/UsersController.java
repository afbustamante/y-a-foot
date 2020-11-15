package net.andresbustamante.yafoot.web.controllers;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.exceptions.LdapException;
import net.andresbustamante.yafoot.services.UserAuthenticationService;
import net.andresbustamante.yafoot.services.UserManagementService;
import net.andresbustamante.yafoot.web.dto.Credentials;
import net.andresbustamante.yafoot.web.dto.User;
import net.andresbustamante.yafoot.web.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.nio.charset.StandardCharsets;

import static org.springframework.http.HttpStatus.*;

@RestController
public class UsersController extends AbstractController implements UsersApi {

    private static final String DIRECTORY_BASIC_ERROR = "directory.basic.error";

    private UserAuthenticationService userAuthenticationService;

    private UserManagementService userManagementService;

    private UserMapper userMapper;

    @Autowired
    public UsersController(UserAuthenticationService userAuthenticationService, UserMapper userMapper,
                           UserManagementService userManagementService,
                           HttpServletRequest request, ApplicationContext applicationContext) {
        super(request, applicationContext);
        this.userAuthenticationService = userAuthenticationService;
        this.userManagementService = userManagementService;
        this.userMapper = userMapper;
        this.request = request;
    }

    @Override
    public ResponseEntity<User> authenticateUser(String email, @Valid User user) {
        try {
            net.andresbustamante.yafoot.model.User authenticatedUser = userAuthenticationService.authenticate(userMapper.map(user));

            if (authenticatedUser == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.accepted().body(userMapper.map(authenticatedUser));
        } catch (ApplicationException e) {
            throw new ResponseStatusException(UNAUTHORIZED, translate(e.getCode(), null));
        }
    }

    @Override
    public ResponseEntity<User> findUser(@Pattern(regexp = "^[0-9A-F]{16}$") @Valid String token) {
        try {
            net.andresbustamante.yafoot.model.User user = userAuthenticationService.findUserByToken(token);

            return (user != null) ? ResponseEntity.ok(userMapper.map(user)) : ResponseEntity.notFound().build();
        } catch (LdapException e) {
            log.error("LDAP error while searching for a user", e);
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DIRECTORY_BASIC_ERROR, null));
        }
    }

    @Override
    public ResponseEntity<Void> generatePasswordResetToken(String email) {
        try {
            net.andresbustamante.yafoot.model.User user = userAuthenticationService.findUserByEmail(email);
            userManagementService.createPasswordResetToken(user);
            return ResponseEntity.status(CREATED).build();
        } catch (LdapException e) {
            log.error("LDAP error while searching for a user", e);
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DIRECTORY_BASIC_ERROR, null));
        } catch (ApplicationException e) {
            log.error("Application error while generating a new password-reset token", e);
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    @Override
    public ResponseEntity<Void> updateUserCredentials(String email, @Valid Credentials credentials) {
        try {
            if (!email.equals(credentials.getUsername())) {
                throw new ResponseStatusException(FORBIDDEN, translate(UNAUTHORISED_USER_ERROR, null));
            }

            net.andresbustamante.yafoot.model.User user = userAuthenticationService.findUserByEmail(email);

            if (user == null) {
                return ResponseEntity.notFound().build();
            }

            user.setPassword(new String(credentials.getPassword(), StandardCharsets.UTF_8));

            if (credentials.getOldPassword() != null) {
                userManagementService.updateUserPassword(user, getUserContext(request));
            } else if (credentials.getValidationToken() != null) {
                userManagementService.resetUserPassword(user, credentials.getValidationToken());
            }
            return ResponseEntity.accepted().build();
        } catch (ApplicationException e) {
            log.error("User not allowed to perform this operation", e);
            throw new ResponseStatusException(FORBIDDEN, translate(UNAUTHORISED_USER_ERROR, null));
        } catch (LdapException e) {
            log.error("LDAP error while updating a user's credentials", e);
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DIRECTORY_BASIC_ERROR, null));
        }
    }
}

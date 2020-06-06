package net.andresbustamante.yafoot.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.exceptions.InvalidCredentialsException;
import net.andresbustamante.yafoot.exceptions.LdapException;
import net.andresbustamante.yafoot.services.UserAuthenticationService;
import net.andresbustamante.yafoot.services.UserManagementService;
import net.andresbustamante.yafoot.web.dto.Credentials;
import net.andresbustamante.yafoot.web.dto.User;
import net.andresbustamante.yafoot.web.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static net.andresbustamante.yafoot.web.controllers.AbstractController.CTX_MESSAGES;
import static org.springframework.http.HttpStatus.*;

@RestController
@CrossOrigin(exposedHeaders = {CTX_MESSAGES})
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
    public ResponseEntity<User> authenticateUser(@Valid User user, String email) {
        try {
            net.andresbustamante.yafoot.model.User authenticatedUser = userAuthenticationService.authenticate(userMapper.map(user));

            if (authenticatedUser == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.accepted().body(userMapper.map(authenticatedUser));
        } catch (InvalidCredentialsException e) {
            return new ResponseEntity<>(buildMessageHeader("invalid.credentials.error", null), HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public ResponseEntity<User> findUser(@Pattern(regexp = "^[0-9A-F]{16}$") @Valid String token) {
        try {
            net.andresbustamante.yafoot.model.User user = userAuthenticationService.findUserByToken(token);

            return (user != null) ? ResponseEntity.ok(userMapper.map(user)) : ResponseEntity.notFound().build();
        } catch (LdapException e) {
            log.error("LDAP error while searching for a user", e);
            return new ResponseEntity<>(buildMessageHeader(DIRECTORY_BASIC_ERROR, null), INTERNAL_SERVER_ERROR);
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
            return new ResponseEntity<>(buildMessageHeader(DIRECTORY_BASIC_ERROR, null), INTERNAL_SERVER_ERROR);
        } catch (ApplicationException e) {
            log.error("Application error while generating a new password-reset token", e);
            return new ResponseEntity<>(buildMessageHeader(DATABASE_BASIC_ERROR, null), INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Void> updateUserCredentials(@Valid Credentials credentials, String email) {
        try {
            if (!email.equals(credentials.getUsername())) {
                return new ResponseEntity<>(buildMessageHeader(UNAUTHORISED_USER_ERROR, null), FORBIDDEN);
            }

            net.andresbustamante.yafoot.model.User user = new net.andresbustamante.yafoot.model.User(email);
            user.setPassword(new String(credentials.getPassword(), StandardCharsets.UTF_8));

            if (credentials.getOldPassword() != null) {
                userManagementService.updateUserPassword(user, getUserContext(request));
            } else if (credentials.getValidationToken() != null) {
                // TODO Implement forgotten password validations
            }
            return ResponseEntity.accepted().build();
        } catch (ApplicationException e) {
            log.error("User not allowed to perform this operation", e);
            return new ResponseEntity<>(buildMessageHeader(UNAUTHORISED_USER_ERROR, null), FORBIDDEN);
        } catch (LdapException e) {
            log.error("LDAP error while updating a user's credentials", e);
            return new ResponseEntity<>(buildMessageHeader(DIRECTORY_BASIC_ERROR, null), INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Optional<ObjectMapper> getObjectMapper() {
        return Optional.empty();
    }

    @Override
    public Optional<HttpServletRequest> getRequest() {
        return Optional.of(request);
    }
}

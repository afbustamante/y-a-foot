package net.andresbustamante.yafoot.users.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.andresbustamante.yafoot.users.services.UserAuthenticationService;
import net.andresbustamante.yafoot.users.services.UserManagementService;
import net.andresbustamante.yafoot.users.services.UserSearchService;
import net.andresbustamante.yafoot.users.web.dto.Credentials;
import net.andresbustamante.yafoot.users.web.dto.User;
import net.andresbustamante.yafoot.users.web.mappers.UserMapper;
import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.commons.exceptions.DirectoryException;
import net.andresbustamante.yafoot.commons.web.controllers.AbstractController;
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

    private final UserSearchService userSearchService;
    private final UserAuthenticationService userAuthenticationService;
    private final UserManagementService userManagementService;
    private final UserMapper userMapper;

    @Autowired
    public UsersController(UserAuthenticationService userAuthenticationService, UserMapper userMapper,
                           UserManagementService userManagementService, UserSearchService userSearchService,
                           HttpServletRequest request, ObjectMapper objectMapper,
                           ApplicationContext applicationContext) {
        super(request, objectMapper, applicationContext);
        this.userAuthenticationService = userAuthenticationService;
        this.userManagementService = userManagementService;
        this.userSearchService = userSearchService;
        this.userMapper = userMapper;
        this.request = request;
    }

    @Override
    public ResponseEntity<User> authenticateUser(String email, @Valid User user) {
        try {
            if (!email.equals(user.getEmail())) {
                return ResponseEntity.badRequest().build();
            }

            net.andresbustamante.yafoot.users.model.User authenticatedUser = userAuthenticationService.authenticate(
                    userMapper.map(user));

            if (authenticatedUser == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.status(CREATED).body(userMapper.map(authenticatedUser));
        } catch (ApplicationException e) {
            throw new ResponseStatusException(UNAUTHORIZED, translate(e.getCode(), null));
        } catch (DirectoryException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DIRECTORY_BASIC_ERROR, null));
        }
    }

    @Override
    public ResponseEntity<User> findUser(@Pattern(regexp = "^[0-9A-F]{16}$") @Valid String token) {
        try {
            net.andresbustamante.yafoot.users.model.User user = userSearchService.findUserByToken(token);

            return (user != null) ? ResponseEntity.ok(userMapper.map(user)) : ResponseEntity.notFound().build();
        } catch (DirectoryException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DIRECTORY_BASIC_ERROR, null));
        }
    }

    @Override
    public ResponseEntity<Void> generatePasswordResetToken(String email) {
        try {
            net.andresbustamante.yafoot.users.model.User user = userSearchService.findUserByEmail(email);

            if (user != null) {
                userManagementService.createPasswordResetToken(user);
                return ResponseEntity.status(CREATED).build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (DirectoryException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DIRECTORY_BASIC_ERROR, null));
        } catch (ApplicationException e) {
            // Error while sending email message
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    @Override
    public ResponseEntity<Void> updateUserCredentials(String email, @Valid Credentials credentials) {
        try {
            if (!email.equals(credentials.getUsername())) {
                throw new ResponseStatusException(FORBIDDEN, translate(UNAUTHORISED_USER_ERROR, null));
            }

            net.andresbustamante.yafoot.users.model.User user = userSearchService.findUserByEmail(email);

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
            throw new ResponseStatusException(FORBIDDEN, translate(UNAUTHORISED_USER_ERROR, null));
        } catch (DirectoryException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DIRECTORY_BASIC_ERROR, null));
        }
    }
}

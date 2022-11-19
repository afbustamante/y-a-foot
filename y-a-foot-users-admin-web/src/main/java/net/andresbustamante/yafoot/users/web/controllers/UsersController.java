package net.andresbustamante.yafoot.users.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.andresbustamante.yafoot.commons.exceptions.ApplicationException;
import net.andresbustamante.yafoot.commons.exceptions.DirectoryException;
import net.andresbustamante.yafoot.commons.web.controllers.AbstractController;
import net.andresbustamante.yafoot.users.services.UserManagementService;
import net.andresbustamante.yafoot.users.services.UserSearchService;
import net.andresbustamante.yafoot.users.web.dto.User;
import net.andresbustamante.yafoot.users.web.mappers.RoleMapper;
import net.andresbustamante.yafoot.users.web.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.text.MessageFormat;

import static org.springframework.http.HttpStatus.*;

@RestController
public class UsersController extends AbstractController implements UsersApi {

    private static final String DIRECTORY_BASIC_ERROR = "directory.basic.error";

    private final UserSearchService userSearchService;
    private final UserManagementService userManagementService;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;

    @Value("${api.users.one.path}")
    private String userApiPath;

    @Autowired
    public UsersController(UserMapper userMapper, RoleMapper roleMapper,
                           UserManagementService userManagementService, UserSearchService userSearchService,
                           HttpServletRequest request, ObjectMapper objectMapper,
                           ApplicationContext applicationContext) {
        super(request, objectMapper, applicationContext);
        this.userManagementService = userManagementService;
        this.userSearchService = userSearchService;
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
    }

    @Override
    public ResponseEntity<Void> createUser(@Valid User user) {
        try {
            userManagementService.createUser(userMapper.map(user), roleMapper.map(user.getMainRole()),
                    getUserContext());
            return ResponseEntity.created(getLocationURI(MessageFormat.format(userApiPath, user.getEmail()))).build();
        } catch (DirectoryException e) {
            log.error("Error while creating a new user", e);
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DIRECTORY_BASIC_ERROR, null));
        }
    }

    @Override
    public ResponseEntity<User> loadUser(String email) {
        try {
            net.andresbustamante.yafoot.users.model.User user = userSearchService.findUserByEmail(email);

            return (user != null) ? ResponseEntity.ok(userMapper.map(user)) : ResponseEntity.notFound().build();
        } catch (DirectoryException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DIRECTORY_BASIC_ERROR, null));
        }
    }

    @Override
    public ResponseEntity<Void> updateUserDetails(String email, @Valid User userDto) {
        try {
            if (!email.equals(userDto.getEmail())) {
                return ResponseEntity.badRequest().build();
            }

            net.andresbustamante.yafoot.users.model.User user = userSearchService.findUserByEmail(email);

            if (user == null) {
                return ResponseEntity.notFound().build();
            }

            userManagementService.updateUser(userMapper.map(userDto), getUserContext());
            return ResponseEntity.accepted().build();
        } catch (ApplicationException e) {
            throw new ResponseStatusException(FORBIDDEN, translate(UNAUTHORISED_USER_ERROR, null));
        } catch (DirectoryException e) {
            log.error("Error while updating a user", e);
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DIRECTORY_BASIC_ERROR, null));
        }
    }

    @Override
    public ResponseEntity<Void> deleteUser(String email) {
        try {
            net.andresbustamante.yafoot.users.model.User user = userSearchService.findUserByEmail(email);

            if (user == null) {
                return ResponseEntity.notFound().build();
            }

            userManagementService.deleteUser(user, getUserContext());
            return ResponseEntity.noContent().build();
        } catch (DirectoryException e) {
            log.error("Error while deleting a new user", e);
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, translate(DIRECTORY_BASIC_ERROR, null));
        }
    }
}

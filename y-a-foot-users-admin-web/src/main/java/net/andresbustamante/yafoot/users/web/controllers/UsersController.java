package net.andresbustamante.yafoot.users.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.andresbustamante.yafoot.commons.exceptions.DirectoryException;
import net.andresbustamante.yafoot.commons.web.controllers.AbstractController;
import net.andresbustamante.yafoot.users.services.UserManagementService;
import net.andresbustamante.yafoot.users.services.UserSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
public class UsersController extends AbstractController implements UsersApi {

    private static final String DIRECTORY_BASIC_ERROR = "directory.basic.error";

    private final UserSearchService userSearchService;
    private final UserManagementService userManagementService;

    @Autowired
    public UsersController(
            UserManagementService userManagementService, UserSearchService userSearchService,
            HttpServletRequest request, ObjectMapper objectMapper, ApplicationContext applicationContext) {
        super(request, objectMapper, applicationContext);
        this.userManagementService = userManagementService;
        this.userSearchService = userSearchService;
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

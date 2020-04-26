package net.andresbustamante.yafoot.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.andresbustamante.yafoot.exceptions.InvalidCredentialsException;
import net.andresbustamante.yafoot.services.UserAuthenticationService;
import net.andresbustamante.yafoot.web.dto.User;
import net.andresbustamante.yafoot.web.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

import static net.andresbustamante.yafoot.web.controllers.AbstractController.CTX_MESSAGES;

@RestController
@CrossOrigin(exposedHeaders = {CTX_MESSAGES})
public class UsersController extends AbstractController implements UsersApi {

    private UserAuthenticationService userAuthenticationService;

    private UserMapper userMapper;

    @Autowired
    public UsersController(UserAuthenticationService userAuthenticationService, UserMapper userMapper, HttpServletRequest request) {
        this.userAuthenticationService = userAuthenticationService;
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
    public Optional<ObjectMapper> getObjectMapper() {
        return Optional.empty();
    }

    @Override
    public Optional<HttpServletRequest> getRequest() {
        return Optional.of(request);
    }
}

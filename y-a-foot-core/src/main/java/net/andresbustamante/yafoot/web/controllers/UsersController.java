package net.andresbustamante.yafoot.web.controllers;

import net.andresbustamante.yafoot.exceptions.InvalidCredentialsException;
import net.andresbustamante.yafoot.model.xs.User;
import net.andresbustamante.yafoot.services.UserAuthenticationService;
import net.andresbustamante.yafoot.web.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@CrossOrigin
public class UsersController extends AbstractController implements UsersApi {

    @Autowired
    private UserAuthenticationService userAuthenticationService;

    @Autowired
    private UserMapper userMapper;

    @Override
    public ResponseEntity<User> authenticateUser(@Valid User user, String email) {
        try {
            net.andresbustamante.yafoot.model.User authenticatedUser = userAuthenticationService.authenticate(userMapper.map(user));

            if (authenticatedUser == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.accepted().body(userMapper.map(authenticatedUser));
        } catch (InvalidCredentialsException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}

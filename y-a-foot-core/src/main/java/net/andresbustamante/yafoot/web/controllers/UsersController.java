package net.andresbustamante.yafoot.web.controllers;

import net.andresbustamante.yafoot.exceptions.InvalidCredentialsException;
import net.andresbustamante.yafoot.model.User;
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
    public ResponseEntity<String> authenticateUser(@Valid net.andresbustamante.yafoot.model.xs.User user, String email) {
        try {
            User u = userMapper.map(user);
            String token = userAuthenticationService.authenticate(u);
            return ResponseEntity.accepted().body(token);
        } catch (InvalidCredentialsException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}

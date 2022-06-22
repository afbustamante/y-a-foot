package net.andresbustamante.yafoot.users.web.services;

import net.andresbustamante.yafoot.commons.exceptions.DirectoryException;
import net.andresbustamante.yafoot.commons.model.enums.RolesEnum;
import net.andresbustamante.yafoot.users.model.User;
import net.andresbustamante.yafoot.users.services.UserSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserSearchService userAuthenticationService;

    private final Logger log = LoggerFactory.getLogger(JwtUserDetailsService.class);

    @Autowired
    public JwtUserDetailsService(UserSearchService userAuthenticationService) {
        this.userAuthenticationService = userAuthenticationService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        try {
            User user = userAuthenticationService.findUserByEmail(username);

            if (user == null) {
                throw new UsernameNotFoundException("Email address not found");
            }

            List<GrantedAuthority> authorities = Collections.singletonList(
                    new SimpleGrantedAuthority(RolesEnum.PLAYER.name())
            );

            return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
        } catch (DirectoryException e) {
            log.error("An LDAP error occurred while looking for user's authentication details", e);
            throw new UsernameNotFoundException("Authentication service not available");
        }
    }
}

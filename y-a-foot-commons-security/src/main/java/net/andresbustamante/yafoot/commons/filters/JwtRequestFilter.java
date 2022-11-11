package net.andresbustamante.yafoot.commons.filters;

import io.jsonwebtoken.ExpiredJwtException;
import net.andresbustamante.yafoot.commons.util.JwtTokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * Request filter for JWT security.
 */
public final class JwtRequestFilter extends OncePerRequestFilter {

    /**
     * Logger for this class.
     */
    private final Logger log = LoggerFactory.getLogger(JwtRequestFilter.class);

    /**
     * JWT utilities.
     */
    private final JwtTokenUtils jwtTokenUtils;

    /**
     * User details service to use for this filter.
     */
    private final UserDetailsService jwtUserDetailsService;

    /**
     * Default constructor.
     *
     * @param jwtTokenUtils JWT utilities
     * @param jwtUserDetailsService User details service
     */
    public JwtRequestFilter(JwtTokenUtils jwtTokenUtils, UserDetailsService jwtUserDetailsService) {
        this.jwtTokenUtils = jwtTokenUtils;
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader(AUTHORIZATION);
        String username = null;
        String jwtToken = null;

        // JWT Token is in the form "Bearer token". Remove Bearer word and get
        // only the Token
        String prefix = "Bearer ";

        if (requestTokenHeader != null && requestTokenHeader.startsWith(prefix)) {
            jwtToken = requestTokenHeader.substring(prefix.length());

            try {
                username = jwtTokenUtils.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                log.error("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                log.error("JWT token has expired");
            }
        } else {
            log.warn("JWT token does not begin with Bearer string");
        }

        if ((username != null) && (SecurityContextHolder.getContext().getAuthentication() == null)
                && jwtTokenUtils.isValidToken(jwtToken, username)) {
            // if token is valid configure Spring Security to manually set authentication
            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            usernamePasswordAuthenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // After setting the Authentication in the context, we specify
            // that the current user is authenticated. So it passes the
            // Spring Security Configurations successfully.
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
        filterChain.doFilter(request, response);
    }
}

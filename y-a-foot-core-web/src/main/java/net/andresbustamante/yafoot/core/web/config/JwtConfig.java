package net.andresbustamante.yafoot.core.web.config;

import net.andresbustamante.yafoot.commons.filters.JwtRequestFilter;
import net.andresbustamante.yafoot.commons.util.JwtAuthenticationEntryPoint;
import net.andresbustamante.yafoot.commons.util.JwtTokenUtils;
import net.andresbustamante.yafoot.core.web.services.JwtUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    /**
     * Builds a new entry point for JWT authentication.
     *
     * @return New JWT authentication entry point
     */
    @Bean
    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint();
    }

    /**
     * Builds a new request filter using JWT utils.
     *
     * @param jwtTokenUtils JWT token utils to use with the new filter
     * @param jwtUserDetailsService User details service to use with the new filter
     * @return New JWT request filter
     */
    @Bean
    public JwtRequestFilter jwtRequestFilter(JwtTokenUtils jwtTokenUtils, JwtUserDetailsService jwtUserDetailsService) {
        return new JwtRequestFilter(jwtTokenUtils, jwtUserDetailsService);
    }
}

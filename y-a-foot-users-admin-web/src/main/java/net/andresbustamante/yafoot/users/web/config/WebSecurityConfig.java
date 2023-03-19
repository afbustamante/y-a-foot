package net.andresbustamante.yafoot.users.web.config;

import net.andresbustamante.yafoot.commons.web.util.CorsConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Spring Security configuration for the users module.
 */
@Configuration
@Profile({"development", "production"})
@EnableWebSecurity
public class WebSecurityConfig {

    @Value("${api.config.rest.allowed-origin}")
    private String allowedOrigin;

    /**
     * Security configuration on URL.
     *
     * @param http
     * @return Security filter chain with updated configuration
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf();
        http.authorizeHttpRequests(authz -> authz
                .anyRequest().authenticated()).httpBasic();
        return http.build();
    }

    /**
     * Builds the bean having the CORS configuration for this Web application.
     *
     * @return CORS configuration source bean
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(allowedOrigin));
        configuration.setAllowedMethods(List.of("HEAD", "OPTIONS", "GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(List.of("Authorization", "Accept", "Cache-Control", "Content-Type", "Origin",
                "X-Requested-With"));
        configuration.setExposedHeaders(List.of("Origin", "Access-Control-Allow-Origin", "Content-Type", "Location"));
        configuration.setMaxAge(CorsConstants.MAX_AGE);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

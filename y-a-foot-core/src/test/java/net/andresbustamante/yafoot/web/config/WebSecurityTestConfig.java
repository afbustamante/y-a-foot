package net.andresbustamante.yafoot.web.config;

import net.andresbustamante.yafoot.web.filters.JwtRequestFilter;
import net.andresbustamante.yafoot.web.services.JwtUserDetailsService;
import net.andresbustamante.yafoot.web.util.JwtAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@TestConfiguration
@Profile({"test"})
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan(basePackages = {
        "net.andresbustamante.yafoot.auth.util",
        "net.andresbustamante.yafoot.core.util",
        "net.andresbustamante.yafoot.web.util",
        "net.andresbustamante.yafoot.web.services"
})
public class WebSecurityTestConfig extends WebSecurityConfigurerAdapter {

    @Value("${users.api.service.path}")
    private String usersApiPath;

    @Value("${players.api.service.path}")
    private String playersApiPath;

    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private JwtRequestFilter jwtRequestFilter;

    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    public WebSecurityTestConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtRequestFilter jwtRequestFilter,
                                 JwtUserDetailsService jwtUserDetailsService) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtRequestFilter = jwtRequestFilter;
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jwtUserDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, usersApiPath).permitAll() // Password-reset request by token
                .antMatchers(HttpMethod.OPTIONS, usersApiPath + "/**").permitAll() // Password-reset token generation
                .antMatchers(HttpMethod.POST, usersApiPath + "/**").permitAll() // Password-reset token generation
                .antMatchers(HttpMethod.PUT, usersApiPath + "/**").permitAll() // Sign-in
                .antMatchers(HttpMethod.PATCH, usersApiPath + "/**").permitAll() // Password update
                .antMatchers(HttpMethod.POST, playersApiPath).permitAll() // Sign-up
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}

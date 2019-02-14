package net.andresbustamante.yafoot.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author andresbustamante
 */
@Configuration
@EnableWebSecurity
@PropertySource("classpath:config.properties")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${ldap.connection.url}")
    private String ldapConnectionUrl;

    @Value("${ldap.base.dn}")
    private String ldapBaseDn;

    @Value("${ldap.users.search.base}")
    private String ldapUsersSearchBase;

    @Value("${ldap.users.search.filter}")
    private String ldapUsersSearchFilter;

    @Value("${ldap.roles.search.base}")
    private String ldapRolesSearchBase;

    @Value("${ldap.roles.search.filter}")
    private String ldapRolesSearchFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // @formatter:off
        auth.ldapAuthentication()
                .userSearchBase(ldapUsersSearchBase)
                .userSearchFilter(ldapUsersSearchFilter)
                .groupSearchBase(ldapRolesSearchBase)
                .groupSearchFilter(ldapRolesSearchFilter)
                .contextSource()
                .url(ldapConnectionUrl)
                .root(ldapBaseDn);
        // @formatter:on
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.authorizeRequests()
                .antMatchers("/", "/css/**", "/images/**", "/start.zul", "/players/signin.zul", "/players/login.zul").permitAll()
                .anyRequest().authenticated();
        http.formLogin()
                .loginPage("/players/login.zul").permitAll()
                .and()
                .logout().logoutSuccessUrl("/");
        // @formatter:on
    }

}

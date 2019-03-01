package net.andresbustamante.yafoot.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
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

    private static final String[] ZK_RESOURCES = {"/zkau/web/**/js/**", "/zkau/web/**/zul/css/**",
            "/zkau/web/**/img/**", "/zul/**"};
    private static final String[] ZK_POST = {"/zkau"};
    private static final String[] WEB_RESOURCES = {"/css/**", "/images/**", "/favicon.ico"};
    private static final String REMOVE_DESKTOP_REGEX = "/zkau\\?dtid=.*&cmd_0=rmDesktop&.*";

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

    @Value("${ldap.manager.dn}")
    private String ldapManagerDn;

    @Value("${ldap.manager.password}")
    private String ldapManagerPassword;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // @formatter:off
        auth.ldapAuthentication()
                .userSearchBase(ldapUsersSearchBase)
                .userSearchFilter(ldapUsersSearchFilter)
                .groupSearchBase(ldapRolesSearchBase)
                .groupSearchFilter(ldapRolesSearchFilter)
                .contextSource()
                .url(ldapConnectionUrl + "/" + ldapBaseDn)
                .managerDn(ldapManagerDn)
                .managerPassword(ldapManagerPassword);
        // @formatter:on
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, ZK_RESOURCES).permitAll()
                .antMatchers(HttpMethod.POST, ZK_POST).permitAll()
                .antMatchers(WEB_RESOURCES).permitAll()
                .regexMatchers(HttpMethod.GET, REMOVE_DESKTOP_REGEX).permitAll()
                .antMatchers("/", "/start.zul", "/signin.zul", "/login.zul").permitAll()
                .anyRequest().authenticated();
        http.formLogin()
                .loginProcessingUrl("/login")
                .loginPage("/login.zul")
                .failureUrl("/login.zul?error=1")
                .defaultSuccessUrl("/")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true);
        // @formatter:on
    }

}

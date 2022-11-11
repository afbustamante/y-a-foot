package net.andresbustamante.yafoot.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

/**
 * LDAP connection configuration.
 */
@Configuration
public class LdapConfig {

    /**
     * LDAP connection template.
     *
     * @param contextSource Context source for the template
     * @return New template for connection on LDAP tree
     */
    @Bean
    public LdapTemplate ldapTemplate(LdapContextSource contextSource) {
        return new LdapTemplate(contextSource);
    }
}

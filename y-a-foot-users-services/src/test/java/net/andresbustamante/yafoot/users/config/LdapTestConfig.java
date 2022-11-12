package net.andresbustamante.yafoot.users.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.test.unboundid.EmbeddedLdapServerFactoryBean;
import org.springframework.ldap.test.unboundid.LdifPopulator;

@Configuration
@ComponentScan(basePackages = {
        "net.andresbustamante.yafoot.users.repository",
        "net.andresbustamante.yafoot.users.util"
})
@PropertySource("classpath:application.properties")
public class LdapTestConfig {

    @Value("${ldap.config.url}")
    private String url;

    @Value("${ldap.config.username}")
    private String userDn;

    @Value("${ldap.config.password}")
    private String password;

    @Bean
    public EmbeddedLdapServerFactoryBean embeddedLdapServer() {
        EmbeddedLdapServerFactoryBean ldapServerFactoryBean = new EmbeddedLdapServerFactoryBean();
        ldapServerFactoryBean.setPartitionName("test");
        ldapServerFactoryBean.setPartitionSuffix("ou=yafoot,dc=andresbustamante,dc=net");
        ldapServerFactoryBean.setPort(6389);
        return ldapServerFactoryBean;
    }

    @Bean
    public LdifPopulator ldifPopulator() {
        LdifPopulator ldifPopulator = new LdifPopulator();
        ldifPopulator.setContextSource(contextSource());
        ldifPopulator.setResource(new ClassPathResource("data.ldif"));
        ldifPopulator.setBase("ou=yafoot,dc=andresbustamante,dc=net");
        ldifPopulator.setClean(true);
        ldifPopulator.setDefaultBase("ou=yafoot,dc=andresbustamante,dc=net");
        return ldifPopulator;
    }

    @Bean
    public LdapContextSource contextSource() {
        LdapContextSource contextSource= new LdapContextSource();
        contextSource.setUrl(url);
        contextSource.setUserDn(userDn);
        contextSource.setPassword(password);
        return contextSource;
    }
}

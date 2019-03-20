package net.andresbustamante.yafoot.config;

import net.andresbustamante.yafoot.web.controllers.*;
import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * @author andresbustamante
 */
@Configuration
public class RsServerConfig {

    @Autowired
    private Bus bus;

    @Bean
    public Server rsServer() {
        JAXRSServerFactoryBean endpoint = new JAXRSServerFactoryBean();
        endpoint.setBus(bus);
        endpoint.setServiceBeans(Arrays.asList(inscriptionsController(),
                joueursController(),
                matchsController(),
                sitesController(),
                utilisateursController()));
        return endpoint.create();
    }

    @Bean
    public InscriptionsController inscriptionsController() {
        return new InscriptionsController();
    }

    @Bean
    public JoueursController joueursController() {
        return new JoueursController();
    }

    @Bean
    public MatchsController matchsController() {
        return new MatchsController();
    }

    @Bean
    public SitesController sitesController() {
        return new SitesController();
    }

    @Bean
    public UtilisateursController utilisateursController() {
        return new UtilisateursController();
    }
}

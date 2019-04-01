package net.andresbustamante.yafoot.config;

import net.andresbustamante.yafoot.web.controllers.*;
import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author andresbustamante
 */
@Configuration
public class RsServerConfig {

    @Autowired
    private Bus bus;

    @Bean
    public ServletRegistrationBean dispatcherServletCxf() {
        return new ServletRegistrationBean(new CXFServlet(), "/api/*");
    }

    @Bean
    public Server rsServer() {
        JAXRSServerFactoryBean endpoint = new JAXRSServerFactoryBean();
        endpoint.setBus(bus);
        endpoint.setFeatures(Collections.singletonList(loggingFeature()));
        endpoint.setServiceBeans(Arrays.asList(inscriptionsController(),
                joueursController(),
                matchsController(),
                sitesController(),
                utilisateursController()));
        return endpoint.create();
    }

    @Bean
    public LoggingFeature loggingFeature() {
        LoggingFeature loggingFeature = new LoggingFeature();
        loggingFeature.setPrettyLogging(true);
        return loggingFeature;
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

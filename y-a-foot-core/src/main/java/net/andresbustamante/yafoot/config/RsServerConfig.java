package net.andresbustamante.yafoot.config;

import net.andresbustamante.yafoot.web.controllers.*;
import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.transport.servlet.CXFServlet;
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

    @Bean
    public ServletRegistrationBean dispatcherServletCxf() {
        return new ServletRegistrationBean(new CXFServlet(), "/api/*");
    }

    @Bean
    public Server rsServer(Bus bus) {
        JAXRSServerFactoryBean endpoint = new JAXRSServerFactoryBean();
        endpoint.setBus(bus);
        endpoint.setFeatures(Collections.singletonList(loggingFeature()));
        endpoint.setServiceBeans(Arrays.asList(registrationsController(),
                playersController(),
                matchesController(),
                sitesController(),
                carsController()));
        return endpoint.create();
    }

    @Bean
    public LoggingFeature loggingFeature() {
        LoggingFeature loggingFeature = new LoggingFeature();
        loggingFeature.setPrettyLogging(true);
        return loggingFeature;
    }

    @Bean
    public RegistrationsController registrationsController() {
        return new RegistrationsController();
    }

    @Bean
    public PlayersController playersController() {
        return new PlayersController();
    }

    @Bean
    public MatchesController matchesController() {
        return new MatchesController();
    }

    @Bean
    public SitesController sitesController() {
        return new SitesController();
    }

    @Bean
    public CarsController carsController() {
        return new CarsController();
    }
}

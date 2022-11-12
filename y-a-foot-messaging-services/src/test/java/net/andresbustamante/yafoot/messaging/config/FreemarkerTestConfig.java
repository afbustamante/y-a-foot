package net.andresbustamante.yafoot.messaging.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

/**
 * Freemarker configuration. Used for creating messages from predefined templates.
 */
@Configuration
public class FreemarkerTestConfig {

    /**
     * Freemarker configuration factory for test templates.
     *
     * @return Configuration factory bean
     */
    @Bean
    public FreeMarkerConfigurationFactoryBean freeMarkerConfiguration() {
        FreeMarkerConfigurationFactoryBean bean = new FreeMarkerConfigurationFactoryBean();
        bean.setTemplateLoaderPath("/templates/");
        return bean;
    }
}

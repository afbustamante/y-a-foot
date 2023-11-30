package net.andresbustamante.yafoot.core.web.config;

import net.andresbustamante.yafoot.core.services.PlayerManagementService;
import net.andresbustamante.yafoot.core.services.PlayerSearchService;
import net.andresbustamante.yafoot.core.web.util.PlayerRegistrationHandlerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * MVC interceptors config.
 */
@Configuration
@Profile({"development", "production"})
public class WebMvcConfig implements WebMvcConfigurer {

    private final PlayerManagementService playerManagementService;
    private final PlayerSearchService playerSearchService;

    /**
     * Public constructor.
     *
     * @param playerManagementService
     * @param playerSearchService
     */
    public WebMvcConfig(final PlayerManagementService playerManagementService,
                        final PlayerSearchService playerSearchService) {
        this.playerManagementService = playerManagementService;
        this.playerSearchService = playerSearchService;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new PlayerRegistrationHandlerInterceptor(playerManagementService, playerSearchService));
    }
}

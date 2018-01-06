package net.andresbustamante.yafoot.rs;

import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 * @author andresbustamante
 */
@ApplicationPath("/rs")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(net.andresbustamante.yafoot.rs.RechercheJoueursRS.class);
        resources.add(net.andresbustamante.yafoot.rs.RechercheMatchsRS.class);
        resources.add(net.andresbustamante.yafoot.rs.RechercheSitesRS.class);
    }
    
}

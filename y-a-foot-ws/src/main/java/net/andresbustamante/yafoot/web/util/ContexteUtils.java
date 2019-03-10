package net.andresbustamante.yafoot.web.util;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.Contexte;
import org.springframework.http.HttpHeaders;

public class ContexteUtils {

    public static Contexte getContexte(HttpHeaders headers) throws ApplicationException {
        String id = headers.getFirst(Contexte.UTILISATEUR);

        if (id != null) {
            Integer idUtilisateur = (headers.containsKey(Contexte.UTILISATEUR)) ? Integer.valueOf(id) : null;
            return new Contexte(idUtilisateur);
        } else {
            throw new ApplicationException("Impossible de determiner l'identifiant de l'utilisateur");
        }
    }

}

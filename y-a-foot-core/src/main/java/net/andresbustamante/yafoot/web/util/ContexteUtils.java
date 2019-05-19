package net.andresbustamante.yafoot.web.util;

import net.andresbustamante.yafoot.exceptions.ApplicationException;
import net.andresbustamante.yafoot.model.Contexte;

import javax.servlet.http.HttpServletRequest;

public class ContexteUtils {

    private ContexteUtils() {}

    public static Contexte getContexte(HttpServletRequest request) throws ApplicationException {
        String id = request.getHeader(Contexte.UTILISATEUR);

        if (id != null) {
            Integer idUtilisateur = (request.getHeader(Contexte.UTILISATEUR) != null) ? Integer.valueOf(id) : null;
            return new Contexte(idUtilisateur);
        } else {
            throw new ApplicationException("Impossible de determiner l'identifiant de l'utilisateur");
        }
    }

}

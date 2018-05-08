package net.andresbustamante.yafoot.util;

import net.andresbustamante.yafoot.model.Contexte;
import org.springframework.http.HttpHeaders;

public class ContexteUtils {

    public static Contexte getContexte(HttpHeaders headers) {
        Integer idUtilisateur = (headers.containsKey(Contexte.UTILISATEUR)) ? Integer.valueOf(headers.getFirst(
                Contexte.UTILISATEUR)) : null;
        return new Contexte(idUtilisateur);
    }

}

package net.andresbustamante.yafoot.util;

import net.andresbustamante.yafoot.model.Contexte;

/**
 * @author andresbustamante
 */
public class ContexteUtils {

    public static Contexte copierInfoContexte(net.andresbustamante.yafoot.xs.Contexte contexte) {
        if (contexte != null) {
            Contexte ctx = new Contexte();

            if (contexte.getUtilisateur() != null) {
                ctx.setEmailUtilisateur(contexte.getUtilisateur().getEmail());
                ctx.setIdUtilisateur(contexte.getUtilisateur().getId());
            }
            return ctx;
        }
        return null;
    }
}

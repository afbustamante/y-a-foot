package net.andresbustamante.yafoot.dao;

import net.andresbustamante.yafoot.model.Joueur;
import org.apache.ibatis.annotations.Param;

import static net.andresbustamante.yafoot.util.DaoConstants.*;

/**
 * Interface de récupération et modification des informations des joueurs en base des données
 *
 * @author andresbustamante
 */
public interface JoueurDAO {

    /**
     * Chercher un joueur par son identifiant dans le système
     *
     * @param idJoueur Identifiant du joueur
     * @return Le joueur correspondant à l'identifiant passé en paramètre. Null si l'identifiant n'est pas utilisé
     */
    Joueur chercherJoueurParId(@Param(ID) Integer idJoueur);

    /**
     * Vérifier si une adresse mail est déjà utilisée par un joueur inscrit
     *
     * @param email Adresse mail à chercher
     * @return True si un joueur existe avec l'adresse mail passée en paramètre
     */
    boolean isJoueurInscrit(@Param(EMAIL) String email);

    /**
     * Créer un joueur en base des données
     *
     * @param joueur Joueur à créer
     */
    void creerJoueur(@Param(JOUEUR) Joueur joueur);

    /**
     * Mettre à jour les informations personnelles d'un joueur passé en paramètre
     *
     * @param joueur Joueur à mettre à jour
     */
    void actualiserJoueur(@Param(JOUEUR) Joueur joueur);

    /**
     * Chercher un joueur en base des données à partir de son adresse mail
     *
     * @param email Adresse mail à chercher
     * @return Joueur associé à l'adresse mail passée en paramètre
     */
    Joueur chercherJoueurParEmail(@Param(EMAIL) String email);

    /**
     * Supprimer définitivement un joueur de la base des données
     *
     * @param joueur Joueur à supprimer
     */
    void supprimerJoueur(@Param(JOUEUR) Joueur joueur);
}

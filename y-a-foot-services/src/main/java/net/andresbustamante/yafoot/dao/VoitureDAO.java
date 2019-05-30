package net.andresbustamante.yafoot.dao;

import net.andresbustamante.yafoot.model.Joueur;
import net.andresbustamante.yafoot.model.Voiture;
import org.apache.ibatis.annotations.Param;

import static net.andresbustamante.yafoot.util.DaoConstants.*;

/**
 * Interface de gestion et récupération des informations des voitures en base des données
 *
 * @author andresbustamante
 */
public interface VoitureDAO {

    /**
     * Récupérer les informations d'une voiture à partir de son identifiant
     *
     * @param id Identifiant de la voiture à chercher
     * @return
     */
    Voiture chercherVoitureParId(@Param(ID) Integer id);

    /**
     * Créer une nouvelle voiture en base de données
     *
     * @param voiture Voiture à enregistrer
     */
    int enregistrerVoiture(@Param(VOITURE) Voiture voiture);

    /**
     * Supprimer toutes les voitures enregistrées par un joueur
     *
     * @param joueur
     */
    int supprimerVoitures(@Param(JOUEUR) Joueur joueur);
}

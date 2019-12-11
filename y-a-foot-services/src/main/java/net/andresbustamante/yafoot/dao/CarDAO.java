package net.andresbustamante.yafoot.dao;

import net.andresbustamante.yafoot.model.Joueur;
import net.andresbustamante.yafoot.model.Voiture;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import static net.andresbustamante.yafoot.util.DaoConstants.*;

/**
 * Interface de gestion et récupération des informations des voitures en base des données
 *
 * @author andresbustamante
 */
public interface CarDAO {

    /**
     * Récupérer les informations d'une voiture à partir de son identifiant
     *
     * @param id Identifiant de la voiture à chercher
     * @return
     */
    Voiture findCarById(@Param(ID) Integer id);

    /**
     * Créer une nouvelle voiture en base de données
     *
     * @param voiture Voiture à enregistrer
     * @param joueur Joueur enregistrant la voiture
     */
    int saveCar(@Param(CAR) Voiture voiture, @Param(PLAYER) Joueur joueur);

    /**
     * Supprimer toutes les voitures enregistrées par un joueur
     *
     * @param joueur
     */
    int deleteCarsForPlayer(@Param(PLAYER) Joueur joueur);

    /**
     * Loads the list of cars that a player has registered in database
     *
     * @param joueur The player to search
     * @return
     */
    List<Voiture> findCarsByPlayer(@Param(PLAYER) Joueur joueur);
}

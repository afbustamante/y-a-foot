package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.VoitureDAO;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.Joueur;
import net.andresbustamante.yafoot.model.Voiture;
import net.andresbustamante.yafoot.services.RechercheVoituresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RechercheVoituresServiceImpl implements RechercheVoituresService {

    @Autowired
    private VoitureDAO voitureDAO;

    @Override
    public List<Voiture> chargerVoituresJoueur(Joueur player, Contexte userContext) throws DatabaseException {
        return voitureDAO.chargerVoituresJoueur(player);
    }
}

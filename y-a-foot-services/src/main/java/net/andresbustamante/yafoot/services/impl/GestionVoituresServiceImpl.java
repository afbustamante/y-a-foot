package net.andresbustamante.yafoot.services.impl;

import net.andresbustamante.yafoot.dao.VoitureDAO;
import net.andresbustamante.yafoot.exceptions.DatabaseException;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.Joueur;
import net.andresbustamante.yafoot.model.Voiture;
import net.andresbustamante.yafoot.services.GestionVoituresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GestionVoituresServiceImpl implements GestionVoituresService {

    @Autowired
    private VoitureDAO voitureDAO;

    @Override
    public int enregistrerVoiture(Voiture voiture, Contexte ctx) throws DatabaseException {
        voitureDAO.enregistrerVoiture(voiture, new Joueur(ctx.getIdUtilisateur()));
        return voiture.getId();
    }
}

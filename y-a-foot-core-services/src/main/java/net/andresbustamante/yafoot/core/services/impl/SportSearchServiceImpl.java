package net.andresbustamante.yafoot.core.services.impl;

import net.andresbustamante.yafoot.commons.exceptions.DatabaseException;
import net.andresbustamante.yafoot.core.dao.SportDao;
import net.andresbustamante.yafoot.core.model.Sport;
import net.andresbustamante.yafoot.core.services.SportSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SportSearchServiceImpl implements SportSearchService {

    private final SportDao sportDAO;

    @Autowired
    public SportSearchServiceImpl(final SportDao sportDAO) {
        this.sportDAO = sportDAO;
    }

    @Override
    public List<Sport> loadSports() throws DatabaseException {
        return sportDAO.loadSports();
    }
}

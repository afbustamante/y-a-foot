package net.andresbustamante.yafoot.core.dao;

import net.andresbustamante.yafoot.core.model.Sport;

import java.util.List;

public interface SportDAO {

    List<Sport> loadSports();
}

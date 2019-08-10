package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.model.Joueur;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {StringMapper.class})
public interface JoueurMapper {

    Joueur map(net.andresbustamante.yafoot.model.xs.Joueur joueur);

    net.andresbustamante.yafoot.model.xs.Joueur map(Joueur joueur);
}

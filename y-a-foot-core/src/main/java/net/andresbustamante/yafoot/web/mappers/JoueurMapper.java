package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.model.Joueur;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {StringMapper.class})
public interface JoueurMapper {

    Joueur toJoueurBean(net.andresbustamante.yafoot.model.xs.Joueur joueur);

    net.andresbustamante.yafoot.model.xs.Joueur toJoueurDTO(Joueur joueur);
}

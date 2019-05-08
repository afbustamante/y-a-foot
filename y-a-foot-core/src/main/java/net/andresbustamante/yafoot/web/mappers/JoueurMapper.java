package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.model.Joueur;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface JoueurMapper {

    JoueurMapper INSTANCE = Mappers.getMapper(JoueurMapper.class);

    Joueur toJoueurBean(net.andresbustamante.yafoot.model.xs.Joueur joueur);

    net.andresbustamante.yafoot.model.xs.Joueur toJoueurDTO(Joueur joueur);
}

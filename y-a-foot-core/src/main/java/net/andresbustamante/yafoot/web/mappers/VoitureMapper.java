package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.model.Voiture;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {JoueurMapper.class, StringMapper.class})
public interface VoitureMapper {

    @Mapping(source = "passagers.joueur", target = "passagers")
    Voiture map(net.andresbustamante.yafoot.model.xs.Voiture voiture);

    @Mapping(source = "passagers", target = "passagers.joueur")
    net.andresbustamante.yafoot.model.xs.Voiture map(Voiture voiture);
}

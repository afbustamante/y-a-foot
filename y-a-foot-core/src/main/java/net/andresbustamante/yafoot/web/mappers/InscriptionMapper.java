package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.model.Inscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {JoueurMapper.class, VoitureMapper.class})
public interface InscriptionMapper {

    @Mappings({
            @Mapping(source = "idMatch", target = "id.idMatch"),
            @Mapping(source = "idMatch", target = "match.id"),
            @Mapping(source = "idJoueur", target = "id.idJoueur")
    })
    Inscription toInscriptionBean(net.andresbustamante.yafoot.model.xs.Inscription inscription);

    @Mappings({
            @Mapping(source = "inscription.id.idMatch", target = "idMatch"),
            @Mapping(source = "inscription.id.idJoueur", target = "idJoueur")
    })
    net.andresbustamante.yafoot.model.xs.Inscription toInscriptionDTO(Inscription inscription);
}

package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.model.Voiture;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface VoitureMapper {

    VoitureMapper INSTANCE = Mappers.getMapper(VoitureMapper.class);

    @Mappings(
            @Mapping(source = "voiture.passagers.joueur", target = "passagers")
    )
    Voiture toVoitureBean(net.andresbustamante.yafoot.model.xs.Voiture voiture);

    @Mappings(
            @Mapping(source = "voiture.passagers", target = "passagers.joueur")
    )
    net.andresbustamante.yafoot.model.xs.Voiture toVoitureDTO(Voiture voiture);
}

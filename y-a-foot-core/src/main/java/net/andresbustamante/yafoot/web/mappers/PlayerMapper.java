package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.config.DtoMapperConfig;
import net.andresbustamante.yafoot.model.Joueur;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = DtoMapperConfig.class, uses = {StringMapper.class})
public interface PlayerMapper {

    @Mapping(target = "telephone", source = "numeroTelephone")
    @Mapping(target = "actif", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "dateDerniereMaj", ignore = true)
    @Mapping(target = "voitures", ignore = true)
    Joueur map(net.andresbustamante.yafoot.model.xs.Joueur joueur);

    @Mapping(target = "numeroTelephone", source = "telephone")
    net.andresbustamante.yafoot.model.xs.Joueur map(Joueur joueur);
}

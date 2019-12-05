package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.config.DtoMapperConfig;
import net.andresbustamante.yafoot.model.Joueur;
import net.andresbustamante.yafoot.model.xs.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = DtoMapperConfig.class, uses = {StringMapper.class})
public interface PlayerMapper {

    @Mapping(target = "nom", source = "surname")
    @Mapping(target = "prenom", source = "firstName")
    @Mapping(target = "motDePasse", source = "password")
    @Mapping(target = "telephone", source = "phoneNumber")
    @Mapping(target = "actif", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "dateDerniereMaj", ignore = true)
    @Mapping(target = "voitures", ignore = true)
    Joueur map(Player player);

    @Mapping(target = "firstName", source = "prenom")
    @Mapping(target = "surname", source = "nom")
    @Mapping(target = "phoneNumber", source = "telephone")
    @Mapping(target = "password", ignore = true)
    Player map(Joueur joueur);
}

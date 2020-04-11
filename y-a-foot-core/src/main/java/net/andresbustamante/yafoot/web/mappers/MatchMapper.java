package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.config.DtoMapperConfig;
import net.andresbustamante.yafoot.model.Match;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = DtoMapperConfig.class, uses = {CarMapper.class, PlayerMapper.class, SiteMapper.class, StringMapper.class,
        RegistrationMapper.class, DateMapper.class})
public interface MatchMapper {

    @Mapping(source = "registrations", target = "inscriptions")
    @Mapping(source = "date", target = "dateMatch")
    @Mapping(source = "numPlayersMin", target = "nbJoueursMin")
    @Mapping(source = "numPlayersMax", target = "nbJoueursMax")
    @Mapping(source = "numRegisteredPlayers", target = "nbJoueursInscrits")
    @Mapping(source = "author", target = "createur")
    @Mapping(source = "carpoolingEnabled", target = "covoiturageActif")
    @Mapping(source = "sharingEnabled", target = "partageActif")
    Match map(net.andresbustamante.yafoot.web.dto.Match match);

    @Mapping(target = "registrations", source = "inscriptions")
    @Mapping(target = "date", source = "dateMatch")
    @Mapping(target = "numPlayersMin", source = "nbJoueursMin")
    @Mapping(target = "numPlayersMax", source = "nbJoueursMax")
    @Mapping(target = "numRegisteredPlayers", source = "nbJoueursInscrits")
    @Mapping(target = "author", source = "createur")
    @Mapping(target = "carpoolingEnabled", source = "covoiturageActif")
    @Mapping(target = "sharingEnabled", source = "partageActif")
    net.andresbustamante.yafoot.web.dto.Match map(Match match);
}

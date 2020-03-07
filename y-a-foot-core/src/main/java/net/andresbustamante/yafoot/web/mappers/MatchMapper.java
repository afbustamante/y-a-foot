package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.config.DtoMapperConfig;
import net.andresbustamante.yafoot.model.Match;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = DtoMapperConfig.class, uses = {CarMapper.class, PlayerMapper.class, SiteMapper.class, StringMapper.class,
        RegistrationMapper.class})
public interface MatchMapper {

    @Mapping(source = "registrations.registration", target = "inscriptions")
    @Mapping(source = "date", target = "dateMatch")
    @Mapping(source = "numPlayersMin", target = "nbJoueursMin")
    @Mapping(source = "numPlayersMax", target = "nbJoueursMax")
    @Mapping(source = "numPlayersRegistered", target = "nbJoueursInscrits")
    @Mapping(source = "author", target = "createur")
    @Mapping(source = "carpoolingEnabled", target = "covoiturageActif")
    @Mapping(source = "sharingEnabled", target = "partageActif")
    Match map(net.andresbustamante.yafoot.model.xs.Match match);

    @Mapping(target = "registrations.registration", source = "inscriptions")
    @Mapping(target = "date", source = "dateMatch")
    @Mapping(target = "numPlayersMin", source = "nbJoueursMin")
    @Mapping(target = "numPlayersMax", source = "nbJoueursMax")
    @Mapping(target = "numPlayersRegistered", source = "nbJoueursInscrits")
    @Mapping(target = "author", source = "createur")
    @Mapping(target = "carpoolingEnabled", source = "covoiturageActif")
    @Mapping(target = "sharingEnabled", source = "partageActif")
    net.andresbustamante.yafoot.model.xs.Match map(Match match);
}

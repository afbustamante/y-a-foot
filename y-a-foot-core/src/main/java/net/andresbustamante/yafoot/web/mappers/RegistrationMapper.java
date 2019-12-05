package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.config.DtoMapperConfig;
import net.andresbustamante.yafoot.model.Inscription;
import net.andresbustamante.yafoot.model.xs.Registration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = DtoMapperConfig.class, uses = {PlayerMapper.class, CarMapper.class})
public interface RegistrationMapper {

    @Mapping(source = "matchId", target = "id.idMatch")
    @Mapping(source = "matchId", target = "match.id")
    @Mapping(source = "playerId", target = "id.idJoueur")
    @Mapping(source = "player", target = "joueur")
    @Mapping(source = "car", target = "voiture")
    Inscription map(Registration registration);

    @Mapping(source = "id.idMatch", target = "matchId")
    @Mapping(source = "id.idJoueur", target = "playerId")
    @Mapping(source = "joueur", target = "player")
    @Mapping(source = "voiture", target = "car")
    Registration map(Inscription inscription);
}

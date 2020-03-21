package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.config.DtoMapperConfig;
import net.andresbustamante.yafoot.model.Inscription;
import net.andresbustamante.yafoot.web.dto.Registration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = DtoMapperConfig.class, uses = {PlayerMapper.class, CarMapper.class})
public interface RegistrationMapper {

    @Mapping(source = "matchId", target = "id.matchId")
    @Mapping(source = "matchId", target = "match.id")
    @Mapping(source = "playerId", target = "id.playerId")
    @Mapping(source = "car", target = "voiture")
    Inscription map(Registration registration);

    @Mapping(source = "id.matchId", target = "matchId")
    @Mapping(source = "id.playerId", target = "playerId")
    @Mapping(source = "voiture", target = "car")
    Registration map(Inscription inscription);
}

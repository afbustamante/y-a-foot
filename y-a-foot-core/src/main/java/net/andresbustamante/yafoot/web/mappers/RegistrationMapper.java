package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.config.SpringMapperConfig;
import net.andresbustamante.yafoot.model.Inscription;
import net.andresbustamante.yafoot.web.dto.Registration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = SpringMapperConfig.class, uses = {PlayerMapper.class, CarMapper.class})
public interface RegistrationMapper {

    @Mapping(source = "matchId", target = "id.matchId")
    @Mapping(source = "matchId", target = "match.id")
    @Mapping(source = "playerId", target = "id.playerId")
    Inscription map(Registration registration);

    @Mapping(source = "id.matchId", target = "matchId")
    @Mapping(source = "id.playerId", target = "playerId")
    Registration map(Inscription inscription);
}

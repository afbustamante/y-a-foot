package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.config.DtoMapperConfig;
import net.andresbustamante.yafoot.model.Inscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = DtoMapperConfig.class, uses = {PlayerMapper.class, CarMapper.class})
public interface RegistrationMapper {

    @Mapping(source = "idMatch", target = "id.idMatch")
    @Mapping(source = "idMatch", target = "match.id")
    @Mapping(source = "idJoueur", target = "id.idJoueur")
    Inscription map(net.andresbustamante.yafoot.model.xs.Inscription inscription);

    @Mapping(source = "id.idMatch", target = "idMatch")
    @Mapping(source = "id.idJoueur", target = "idJoueur")
    net.andresbustamante.yafoot.model.xs.Inscription map(Inscription inscription);
}

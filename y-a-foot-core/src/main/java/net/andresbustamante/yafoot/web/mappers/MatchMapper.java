package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.config.DtoMapperConfig;
import net.andresbustamante.yafoot.model.Match;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = DtoMapperConfig.class, uses = {CarMapper.class, UserMapper.class, PlayerMapper.class, StringMapper.class, SiteMapper.class, RegistrationMapper.class})
public interface MatchMapper {

    @Mapping(source = "inscriptions.inscription", target = "inscriptions")
    @Mapping(source = "date", target = "dateMatch")
    Match map(net.andresbustamante.yafoot.model.xs.Match match);

    @Mapping(source = "inscriptions", target = "inscriptions.inscription")
    @Mapping(source = "dateMatch", target = "date")
    net.andresbustamante.yafoot.model.xs.Match map(Match match);
}

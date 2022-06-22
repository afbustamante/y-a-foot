package net.andresbustamante.yafoot.core.web.mappers;

import net.andresbustamante.yafoot.commons.web.mappers.StringMapper;
import net.andresbustamante.yafoot.commons.web.config.SpringMapperConfig;
import net.andresbustamante.yafoot.core.model.Match;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = SpringMapperConfig.class, uses = {
        CarMapper.class, PlayerMapper.class, SiteMapper.class, StringMapper.class,
        SportMapper.class, RegistrationMapper.class
})
public interface MatchMapper {

    @Mapping(target = "registrations", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "lastUpdateDate", ignore = true)
    Match map(net.andresbustamante.yafoot.web.dto.Match match);

    net.andresbustamante.yafoot.web.dto.Match map(Match match);

    List<net.andresbustamante.yafoot.web.dto.Match> map(List<Match> matches);
}

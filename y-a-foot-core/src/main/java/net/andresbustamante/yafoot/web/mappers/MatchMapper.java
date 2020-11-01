package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.config.SpringMapperConfig;
import net.andresbustamante.yafoot.model.Match;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = SpringMapperConfig.class, uses = {CarMapper.class, PlayerMapper.class, SiteMapper.class, StringMapper.class,
        RegistrationMapper.class})
public interface MatchMapper {

    @Mapping(target = "registrations", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "lastUpdateDate", ignore = true)
    Match map(net.andresbustamante.yafoot.web.dto.Match match);

    net.andresbustamante.yafoot.web.dto.Match map(Match match);

    List<net.andresbustamante.yafoot.web.dto.Match> map(List<Match> matches);
}

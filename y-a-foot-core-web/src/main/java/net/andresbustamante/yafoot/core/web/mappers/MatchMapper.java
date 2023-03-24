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

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "creator", ignore = true)
    @Mapping(target = "registrations", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "modificationDate", ignore = true)
    Match map(net.andresbustamante.yafoot.web.dto.MatchForm match);

    /**
     * Bean to DTO mapping for a match object.
     *
     * @param match Match bean to map
     * @return Match DTO
     */
    net.andresbustamante.yafoot.web.dto.Match map(Match match);

    /**
     * Bean list to DTO list mapping for a list of matches.
     *
     * @param matches Matches list to map
     * @return Resulting DTO list
     */
    List<net.andresbustamante.yafoot.web.dto.Match> map(List<Match> matches);
}

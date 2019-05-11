package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.model.Match;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {VoitureMapper.class})
public interface MatchMapper {

    @Mappings({
            @Mapping(source = "match.inscriptions.inscription", target = "inscriptions"),
            @Mapping(source = "match.date", target = "dateMatch")
    })
    Match toMatchBean(net.andresbustamante.yafoot.model.xs.Match match);

    @Mappings({
            @Mapping(source = "match.inscriptions", target = "inscriptions.inscription"),
            @Mapping(source = "match.dateMatch", target = "date")
    })
    net.andresbustamante.yafoot.model.xs.Match toMatchDTO(Match match);
}

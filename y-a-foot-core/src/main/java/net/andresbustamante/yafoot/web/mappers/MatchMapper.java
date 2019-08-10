package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.model.Match;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {VoitureMapper.class, JoueurMapper.class, StringMapper.class})
public interface MatchMapper {

    @Mapping(source = "inscriptions.inscription", target = "inscriptions")
    @Mapping(source = "date", target = "dateMatch")
    Match map(net.andresbustamante.yafoot.model.xs.Match match);

    @Mapping(source = "inscriptions", target = "inscriptions.inscription")
    @Mapping(source = "dateMatch", target = "date")
    net.andresbustamante.yafoot.model.xs.Match map(Match match);
}

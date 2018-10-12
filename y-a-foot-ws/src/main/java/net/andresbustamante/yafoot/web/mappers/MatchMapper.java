package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.model.Match;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MatchMapper {

    MatchMapper INSTANCE = Mappers.getMapper(MatchMapper.class);

    @Mappings({
            @Mapping(source = "match.inscriptions.inscription", target = "inscriptions")
    })
    Match toMatchBean(net.andresbustamante.yafoot.model.xs.Match match);

    @Mappings({
            @Mapping(source = "match.inscriptions", target = "inscriptions.inscription")
    })
    net.andresbustamante.yafoot.model.xs.Match toMatchDTO(Match match);
}

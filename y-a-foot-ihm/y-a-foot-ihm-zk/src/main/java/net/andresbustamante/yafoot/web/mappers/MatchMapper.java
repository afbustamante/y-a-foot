package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.model.xs.Match;
import net.andresbustamante.yafoot.web.vo.MatchVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author andresbustamante
 */
@Mapper
public interface MatchMapper {

    MatchMapper INSTANCE = Mappers.getMapper(MatchMapper.class);

    Match toMatchDTO(MatchVO matchVO);

    MatchVO toMatchVO(Match match);
}

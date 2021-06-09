package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.core.model.enums.SportEnum;
import net.andresbustamante.yafoot.web.config.SpringMapperConfig;
import net.andresbustamante.yafoot.web.dto.Sport;
import net.andresbustamante.yafoot.web.dto.SportCode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = SpringMapperConfig.class)
public interface SportMapper {

    @Mapping(target = "code", source = "code", qualifiedByName = "mapSportCode")
    Sport map(net.andresbustamante.yafoot.core.model.Sport sport);

    List<Sport> map(List<net.andresbustamante.yafoot.core.model.Sport> sports);

    SportCode map(SportEnum value);

    SportEnum map(SportCode code);

    default SportCode mapSportCode(String code) {
        if (code != null) {
            return SportCode.valueOf(code);
        }
        return null;
    }
}

package net.andresbustamante.yafoot.core.web.mappers;

import net.andresbustamante.yafoot.core.model.enums.SportEnum;
import net.andresbustamante.yafoot.commons.web.config.SpringMapperConfig;
import net.andresbustamante.yafoot.web.dto.Sport;
import net.andresbustamante.yafoot.web.dto.SportCode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = SpringMapperConfig.class)
public interface SportMapper {

    @Mapping(target = "code", source = "code", qualifiedByName = "mapSportCode")
    Sport map(net.andresbustamante.yafoot.core.model.Sport sport);

    /**
     * Maps a list of sport beans into a list of DTOs.
     *
     * @param sports Sport beans to map
     * @return Resulting DTO list
     */
    List<Sport> map(List<net.andresbustamante.yafoot.core.model.Sport> sports);

    /**
     * Maps a sport value into an enumerated sport.
     *
     * @param value Sport value to map
     * @return Resulting sport enum
     */
    SportCode map(SportEnum value);

    /**
     * Maps a sport code into a sport enumerated value.
     *
     * @param code Code to map
     * @return Sport enum
     */
    SportEnum map(SportCode code);

    /**
     * Gets the enumeration code for a given sport code.
     *
     * @param code Code to search
     * @return SportCode for the given code
     */
    default SportCode mapSportCode(String code) {
        if (code != null) {
            return SportCode.valueOf(code);
        }
        return null;
    }
}

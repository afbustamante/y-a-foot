package net.andresbustamante.yafoot.core.web.mappers;

import net.andresbustamante.yafoot.commons.web.config.SpringMapperConfig;
import net.andresbustamante.yafoot.core.model.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for a minimalist player DTO.
 */
@Mapper(config = SpringMapperConfig.class)
public interface BasicPlayerMapper {

    /**
     * Maps a player bean into a minimalist player DTO.
     *
     * @param player Player to map
     * @return Resulting DTO
     */
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "phoneNumber", ignore = true)
    @Mapping(target = "preferredLanguage", ignore = true)
    net.andresbustamante.yafoot.web.dto.Player map(Player player);
}

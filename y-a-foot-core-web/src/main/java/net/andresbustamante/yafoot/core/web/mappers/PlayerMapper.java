package net.andresbustamante.yafoot.core.web.mappers;

import net.andresbustamante.yafoot.commons.web.mappers.StringMapper;
import net.andresbustamante.yafoot.commons.web.config.SpringMapperConfig;
import net.andresbustamante.yafoot.core.model.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = SpringMapperConfig.class, uses = {StringMapper.class})
public interface PlayerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "modificationDate", ignore = true)
    Player map(net.andresbustamante.yafoot.web.dto.PlayerForm player);

    /**
     * DTO mapping for a player bean.
     *
     * @param player Bean to map
     * @return Resulting DTO
     */
    net.andresbustamante.yafoot.web.dto.Player map(Player player);

    /**
     * Maps a list of player beans into a list of DTOs.
     *
     * @param players Players to map
     * @return Resulting DTO list
     */
    List<net.andresbustamante.yafoot.web.dto.Player> map(List<Player> players);
}

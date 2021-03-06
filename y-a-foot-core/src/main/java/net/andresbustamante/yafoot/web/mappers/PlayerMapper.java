package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.web.config.SpringMapperConfig;
import net.andresbustamante.yafoot.core.model.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = SpringMapperConfig.class, uses = {StringMapper.class})
public interface PlayerMapper {

    @Mapping(target = "active", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "lastUpdateDate", ignore = true)
    @Mapping(target = "cars", ignore = true)
    @Mapping(target = "preferredLanguage", ignore = true)
    Player map(net.andresbustamante.yafoot.web.dto.Player player);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "token", ignore = true)
    net.andresbustamante.yafoot.web.dto.Player map(Player player);
}

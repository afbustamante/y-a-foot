package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.config.SpringMapperConfig;
import net.andresbustamante.yafoot.model.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = SpringMapperConfig.class, uses = {StringMapper.class})
public interface PlayerMapper {

    @Mapping(target = "active", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "lastUpdateDate", ignore = true)
    @Mapping(target = "cars", ignore = true)
    Player map(net.andresbustamante.yafoot.web.dto.Player player);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "token", ignore = true)
    net.andresbustamante.yafoot.web.dto.Player map(Player player);
}

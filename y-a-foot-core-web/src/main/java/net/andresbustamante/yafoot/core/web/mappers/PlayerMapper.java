package net.andresbustamante.yafoot.core.web.mappers;

import net.andresbustamante.yafoot.commons.web.mappers.StringMapper;
import net.andresbustamante.yafoot.commons.web.config.SpringMapperConfig;
import net.andresbustamante.yafoot.core.model.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

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

    List<net.andresbustamante.yafoot.web.dto.Player> map(List<Player> players);
}
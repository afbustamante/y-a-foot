package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.config.SpringMapperConfig;
import net.andresbustamante.yafoot.model.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = SpringMapperConfig.class, uses = {StringMapper.class})
public interface PlayerMapper {

    @Mapping(target = "active", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "dateDerniereMaj", ignore = true)
    @Mapping(target = "voitures", ignore = true)
    Player map(net.andresbustamante.yafoot.web.dto.Player player);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "token", ignore = true)
    net.andresbustamante.yafoot.web.dto.Player map(Player player);
}

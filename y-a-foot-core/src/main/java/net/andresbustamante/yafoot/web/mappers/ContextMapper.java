package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.config.SpringMapperConfig;
import net.andresbustamante.yafoot.model.UserContext;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = SpringMapperConfig.class)
public interface ContextMapper {

    @Mapping(source = "player.email", target = "username")
    @Mapping(target = "timezone", ignore = true)
    UserContext map(net.andresbustamante.yafoot.web.dto.UserContext userContext);
}

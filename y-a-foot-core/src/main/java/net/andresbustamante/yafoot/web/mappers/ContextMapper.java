package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.config.DtoMapperConfig;
import net.andresbustamante.yafoot.model.UserContext;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = DtoMapperConfig.class)
public interface ContextMapper {

    @Mapping(source = "player.id", target = "userId")
    @Mapping(source = "player.email", target = "userEmail")
    @Mapping(target = "timezone", ignore = true)
    UserContext map(net.andresbustamante.yafoot.model.xs.UserContext userContext);
}

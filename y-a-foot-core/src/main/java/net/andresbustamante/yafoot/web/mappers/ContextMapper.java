package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.config.DtoMapperConfig;
import net.andresbustamante.yafoot.model.Contexte;
import net.andresbustamante.yafoot.model.xs.UserContext;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = DtoMapperConfig.class)
public interface ContextMapper {

    @Mapping(source = "user.id", target = "idUtilisateur")
    @Mapping(source = "user.email", target = "emailUtilisateur")
    @Mapping(target = "timezone", ignore = true)
    Contexte map(UserContext userContext);
}

package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.config.DtoMapperConfig;
import net.andresbustamante.yafoot.model.Contexte;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = DtoMapperConfig.class)
public interface ContextMapper {

    @Mapping(source = "utilisateur.id", target = "idUtilisateur")
    @Mapping(source = "utilisateur.email", target = "emailUtilisateur")
    @Mapping(target = "timezone", ignore = true)
    Contexte map(net.andresbustamante.yafoot.model.xs.Contexte contexte);
}

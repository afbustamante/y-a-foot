package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.model.Contexte;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ContexteMapper {

    @Mappings({@Mapping(source = "contexte.utilisateur.id", target = "idUtilisateur"),
            @Mapping(source = "contexte.utilisateur.email", target = "emailUtilisateur")})
    Contexte toContexteBean(net.andresbustamante.yafoot.model.xs.Contexte contexte);
}

package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.model.Contexte;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ContexteMapper {

    @Mapping(source = "utilisateur.id", target = "idUtilisateur")
    @Mapping(source = "utilisateur.email", target = "emailUtilisateur")
    Contexte map(net.andresbustamante.yafoot.model.xs.Contexte contexte);
}

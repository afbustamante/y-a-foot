package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.model.Contexte;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ContexteMapper {

    ContexteMapper INSTANCE = Mappers.getMapper(ContexteMapper.class);

    @Mappings({@Mapping(source = "contexte.utilisateur.id", target = "idUtilisateur"),
            @Mapping(source = "contexte.utilisateur.email", target = "emailUtilisateur")})
    Contexte toContexteBean(net.andresbustamante.yafoot.model.xs.Contexte contexte);
}

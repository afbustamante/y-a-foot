package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.config.DtoMapperConfig;
import net.andresbustamante.yafoot.model.Utilisateur;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = DtoMapperConfig.class, uses = {StringMapper.class})
public interface UserMapper {

    @Mapping(target = "nom", ignore = true)
    @Mapping(target = "prenom", ignore = true)
    @Mapping(target = "telephone", ignore = true)
    @Mapping(target = "actif", ignore = true)
    Utilisateur map(net.andresbustamante.yafoot.model.xs.Utilisateur usr);

    net.andresbustamante.yafoot.model.xs.Utilisateur map(Utilisateur usr);
}

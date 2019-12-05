package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.config.DtoMapperConfig;
import net.andresbustamante.yafoot.model.Utilisateur;
import net.andresbustamante.yafoot.model.xs.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = DtoMapperConfig.class, uses = {StringMapper.class})
public interface UserMapper {

    @Mapping(target = "motDePasse", source = "password")
    @Mapping(target = "nom", ignore = true)
    @Mapping(target = "prenom", ignore = true)
    @Mapping(target = "telephone", ignore = true)
    @Mapping(target = "actif", ignore = true)
    Utilisateur map(User usr);

    @Mapping(target = "password", ignore = true)
    User map(Utilisateur usr);
}

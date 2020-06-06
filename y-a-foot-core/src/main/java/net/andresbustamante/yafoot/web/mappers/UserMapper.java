package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.config.SpringMapperConfig;
import net.andresbustamante.yafoot.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = SpringMapperConfig.class, uses = {StringMapper.class})
public interface UserMapper {

    @Mapping(target = "surname", ignore = true)
    @Mapping(target = "firstName", ignore = true)
    @Mapping(target = "preferredLanguage", ignore = true)
    User map(net.andresbustamante.yafoot.web.dto.User usr);

    @Mapping(target = "password", ignore = true)
    net.andresbustamante.yafoot.web.dto.User map(User usr);
}

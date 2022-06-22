package net.andresbustamante.yafoot.users.web.mappers;

import net.andresbustamante.yafoot.users.model.User;
import net.andresbustamante.yafoot.commons.web.config.SpringMapperConfig;
import net.andresbustamante.yafoot.commons.web.mappers.StringMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = SpringMapperConfig.class, uses = {StringMapper.class})
public interface UserMapper {

    @Mapping(target = "preferredLanguage", ignore = true)
    User map(net.andresbustamante.yafoot.users.web.dto.User usr);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "mainRole", ignore = true)
    net.andresbustamante.yafoot.users.web.dto.User map(User usr);
}

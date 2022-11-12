package net.andresbustamante.yafoot.core.web.mappers;

import net.andresbustamante.yafoot.users.model.User;
import net.andresbustamante.yafoot.commons.web.config.SpringMapperConfig;
import net.andresbustamante.yafoot.commons.web.mappers.StringMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = SpringMapperConfig.class, uses = {StringMapper.class})
public interface UserMapper {

    @Mapping(target = "preferredLanguage", ignore = true)
    User map(net.andresbustamante.yafoot.users.dto.User usr);

    @Mapping(target = "mainRole", constant = "PLAYER")
    net.andresbustamante.yafoot.users.dto.User map(User usr);
}

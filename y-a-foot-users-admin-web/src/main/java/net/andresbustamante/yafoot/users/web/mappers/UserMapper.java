package net.andresbustamante.yafoot.users.web.mappers;

import net.andresbustamante.yafoot.commons.web.config.SpringMapperConfig;
import net.andresbustamante.yafoot.users.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = SpringMapperConfig.class)
public interface UserMapper {

    /**
     * Maps a DTO user to a model user.
     *
     * @param form User form data to map
     * @return Resulting model user
     */
    @Mapping(target = "preferredLanguage", ignore = true)
    @Mapping(target = "email", ignore = true)
    User map(net.andresbustamante.yafoot.users.web.dto.UserForm form);

    /**
     * Maps a user (model) to a DTO user.
     *
     * @param user User to map
     * @return Resulting DTO
     */
    net.andresbustamante.yafoot.users.web.dto.User map(User user);
}

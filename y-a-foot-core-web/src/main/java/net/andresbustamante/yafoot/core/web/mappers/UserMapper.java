package net.andresbustamante.yafoot.core.web.mappers;

import net.andresbustamante.yafoot.commons.web.config.SpringMapperConfig;
import net.andresbustamante.yafoot.commons.web.mappers.StringMapper;
import net.andresbustamante.yafoot.users.model.User;
import org.mapstruct.Mapper;

@Mapper(config = SpringMapperConfig.class, uses = {StringMapper.class})
public interface UserMapper {

    /**
     * Maps a model user into a DTO form user.
     *
     * @param user Model user to map
     * @return Resulting DTO
     */
    net.andresbustamante.yafoot.users.web.dto.UserForm map(User user);
}

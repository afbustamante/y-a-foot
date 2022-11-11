package net.andresbustamante.yafoot.users.web.mappers;

import net.andresbustamante.yafoot.users.model.enums.RolesEnum;
import net.andresbustamante.yafoot.commons.web.config.SpringMapperConfig;
import net.andresbustamante.yafoot.users.web.dto.Role;
import org.mapstruct.Mapper;

@Mapper(config = SpringMapperConfig.class)
public interface RoleMapper {

    /**
     * Maps a role value into a role from the model enum.
     *
     * @param role Role value to map
     * @return Role from the model enum
     */
    RolesEnum map(Role role);
}

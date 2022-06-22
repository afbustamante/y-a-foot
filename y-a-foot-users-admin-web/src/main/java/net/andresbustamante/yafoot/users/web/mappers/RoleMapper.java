package net.andresbustamante.yafoot.users.web.mappers;

import net.andresbustamante.yafoot.commons.model.enums.RolesEnum;
import net.andresbustamante.yafoot.commons.web.config.SpringMapperConfig;
import net.andresbustamante.yafoot.users.web.dto.Role;
import org.mapstruct.Mapper;

@Mapper(config = SpringMapperConfig.class)
public interface RoleMapper {

    RolesEnum map(Role role);
}

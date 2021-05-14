package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.web.config.SpringMapperConfig;
import net.andresbustamante.yafoot.core.model.Registration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = SpringMapperConfig.class, uses = {PlayerMapper.class, CarMapper.class})
public interface RegistrationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "carConfirmed", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "lastUpdateDate", ignore = true)
    Registration map(net.andresbustamante.yafoot.web.dto.Registration registration);

    net.andresbustamante.yafoot.web.dto.Registration map(Registration registration);

    List<net.andresbustamante.yafoot.web.dto.Registration> map(List<Registration> registration);
}

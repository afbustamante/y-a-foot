package net.andresbustamante.yafoot.core.web.mappers;

import net.andresbustamante.yafoot.commons.web.config.SpringMapperConfig;
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

    /**
     * Bean to DTO mapping for a registration object.
     *
     * @param registration Registration bean to map
     * @return Mapped registration DTO
     */
    net.andresbustamante.yafoot.web.dto.Registration map(Registration registration);

    /**
     * Bean to DTO mapping for a registration list.
     *
     * @param registration Registration beans to map
     * @return Mapped registration list
     */
    List<net.andresbustamante.yafoot.web.dto.Registration> map(List<Registration> registration);
}

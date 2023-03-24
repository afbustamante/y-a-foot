package net.andresbustamante.yafoot.core.web.mappers;

import net.andresbustamante.yafoot.commons.web.config.SpringMapperConfig;
import net.andresbustamante.yafoot.web.dto.GpsCoordinates;
import org.mapstruct.Mapper;

@Mapper(config = SpringMapperConfig.class)
public interface GpsCoordinatesMapper {

    /**
     * Maps simple GPS coordinates from model to DTO.
     *
     * @param gpsCoordinates Model bean to map
     * @return Resulting DTO
     */
    GpsCoordinates map(net.andresbustamante.yafoot.commons.model.GpsCoordinates gpsCoordinates);

    /**
     * Maps simple GPS coordinates from DTO to model bean.
     *
     * @param gpsCoordinates DTO to map
     * @return Resulting model bean
     */
    net.andresbustamante.yafoot.commons.model.GpsCoordinates map(GpsCoordinates gpsCoordinates);
}

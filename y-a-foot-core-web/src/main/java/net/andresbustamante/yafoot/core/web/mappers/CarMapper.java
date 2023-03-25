package net.andresbustamante.yafoot.core.web.mappers;

import net.andresbustamante.yafoot.commons.web.mappers.StringMapper;
import net.andresbustamante.yafoot.commons.web.config.SpringMapperConfig;
import net.andresbustamante.yafoot.core.model.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = SpringMapperConfig.class, uses = {BasicPlayerMapper.class, StringMapper.class})
public interface CarMapper {

    /**
     * Maps a DTO into a car bean.
     *
     * @param car DTO to map
     * @return Resulting car bean
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "numPassengers", ignore = true)
    @Mapping(target = "driver", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "modificationDate", ignore = true)
    Car map(net.andresbustamante.yafoot.web.dto.CarForm car);

    /**
     * Maps a car bean into a DTO.
     *
     * @param car Bean to map
     * @return Resulting car DTO
     */
    net.andresbustamante.yafoot.web.dto.Car map(Car car);

    /**
     * Maps a list of car beans into a list of DTOs.
     *
     * @param cars Beans to map
     * @return Resulting DTO list
     */
    List<net.andresbustamante.yafoot.web.dto.Car> map(List<Car> cars);
}

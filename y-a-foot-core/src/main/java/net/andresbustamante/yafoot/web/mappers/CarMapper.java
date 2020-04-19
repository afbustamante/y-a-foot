package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.config.SpringMapperConfig;
import net.andresbustamante.yafoot.model.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = SpringMapperConfig.class, uses = {PlayerMapper.class, StringMapper.class})
public interface CarMapper {

    Car map(net.andresbustamante.yafoot.web.dto.Car car);

    net.andresbustamante.yafoot.web.dto.Car map(Car car);
    
    List<net.andresbustamante.yafoot.web.dto.Car> map(List<Car> cars);
}

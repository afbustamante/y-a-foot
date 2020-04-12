package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.config.SpringMapperConfig;
import net.andresbustamante.yafoot.model.Voiture;
import net.andresbustamante.yafoot.web.dto.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = SpringMapperConfig.class, uses = {PlayerMapper.class, StringMapper.class})
public interface CarMapper {

    @Mapping(source = "name", target = "nom")
    @Mapping(source = "numSeats", target = "nbPlaces")
    @Mapping(source = "driver", target = "chauffeur")
    @Mapping(source = "passengers", target = "passagers")
    Voiture map(Car car);

    @Mapping(target = "name", source = "nom")
    @Mapping(target = "numSeats", source = "nbPlaces")
    @Mapping(target = "driver", source = "chauffeur")
    @Mapping(target = "passengers", source = "passagers")
    Car map(Voiture voiture);
    
    List<Car> map(List<Voiture> voitures);
}

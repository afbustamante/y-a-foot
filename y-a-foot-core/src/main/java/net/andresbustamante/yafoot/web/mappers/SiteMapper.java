package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.config.SpringMapperConfig;
import net.andresbustamante.yafoot.model.Site;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = SpringMapperConfig.class)
public interface SiteMapper {

    @Mapping(source = "name", target = "nom")
    @Mapping(source = "address", target = "adresse")
    @Mapping(source = "phoneNumber", target = "telephone")
    @Mapping(source = "location", target = "localisation")
    Site map(net.andresbustamante.yafoot.web.dto.Site site);

    @Mapping(target = "name", source = "nom")
    @Mapping(target = "address", source = "adresse")
    @Mapping(target = "phoneNumber", source = "telephone")
    @Mapping(target = "location", source = "localisation")
    net.andresbustamante.yafoot.web.dto.Site map(Site site);
}

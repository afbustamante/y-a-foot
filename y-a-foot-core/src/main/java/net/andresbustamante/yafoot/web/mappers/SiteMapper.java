package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.config.DtoMapperConfig;
import net.andresbustamante.yafoot.model.Site;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = DtoMapperConfig.class)
public interface SiteMapper {

    @Mapping(source = "numeroTelephone", target = "telephone")
    Site map(net.andresbustamante.yafoot.model.xs.Site site);

    @Mapping(source = "telephone", target = "numeroTelephone")
    net.andresbustamante.yafoot.model.xs.Site map(Site site);
}

package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.config.SpringMapperConfig;
import net.andresbustamante.yafoot.model.Site;
import org.mapstruct.Mapper;

@Mapper(config = SpringMapperConfig.class)
public interface SiteMapper {

    Site map(net.andresbustamante.yafoot.web.dto.Site site);

    net.andresbustamante.yafoot.web.dto.Site map(Site site);
}

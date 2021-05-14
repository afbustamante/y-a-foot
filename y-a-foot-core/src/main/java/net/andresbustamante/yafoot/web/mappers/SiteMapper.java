package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.web.config.SpringMapperConfig;
import net.andresbustamante.yafoot.core.model.Site;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = SpringMapperConfig.class)
public interface SiteMapper {

    Site map(net.andresbustamante.yafoot.web.dto.Site site);

    net.andresbustamante.yafoot.web.dto.Site map(Site site);

    List<net.andresbustamante.yafoot.web.dto.Site> map(List<Site> site);
}

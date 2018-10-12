package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.model.Site;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SiteMapper {

    SiteMapper INSTANCE = Mappers.getMapper(SiteMapper.class);

    Site toSiteBean(net.andresbustamante.yafoot.model.xs.Site site);

    net.andresbustamante.yafoot.model.xs.Site toSiteDTO(Site site);
}

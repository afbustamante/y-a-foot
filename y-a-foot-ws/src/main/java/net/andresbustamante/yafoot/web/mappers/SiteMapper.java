package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.model.Site;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SiteMapper {

    SiteMapper INSTANCE = Mappers.getMapper(SiteMapper.class);

    @Mapping(source = "site.numeroTelephone", target = "telephone")
    Site toSiteBean(net.andresbustamante.yafoot.model.xs.Site site);

    @Mapping(source = "site.telephone", target = "numeroTelephone")
    net.andresbustamante.yafoot.model.xs.Site toSiteDTO(Site site);
}

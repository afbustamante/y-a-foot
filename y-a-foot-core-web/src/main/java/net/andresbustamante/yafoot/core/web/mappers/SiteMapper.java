package net.andresbustamante.yafoot.core.web.mappers;

import net.andresbustamante.yafoot.commons.web.config.SpringMapperConfig;
import net.andresbustamante.yafoot.core.model.Site;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = SpringMapperConfig.class)
public interface SiteMapper {

    /**
     * DTO to bean mapping from a site form.
     *
     * @param site Site form data to map
     * @return Site bean
     */
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "lastUpdateDate", ignore = true)
    Site map(net.andresbustamante.yafoot.web.dto.Site site);

    /**
     * Bean to DTO mapping for a site.
     *
     * @param site Site bean to map
     * @return Site DTO
     */
    net.andresbustamante.yafoot.web.dto.Site map(Site site);

    /**
     * Bean list to DTO list mapping.
     *
     * @param site Site list to map
     * @return Resulting DTO list
     */
    List<net.andresbustamante.yafoot.web.dto.Site> map(List<Site> site);
}

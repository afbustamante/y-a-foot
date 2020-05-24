package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.config.SpringMapperConfig;
import net.andresbustamante.yafoot.model.Match;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = SpringMapperConfig.class, uses = {SiteMapper.class})
public interface BasicMatchMapper {

    @Mapping(target = "author", ignore = true)
    @Mapping(target = "sharingEnabled", source = "codeSharingEnabled")
    @Mapping(target = "registrations", ignore = true)
    net.andresbustamante.yafoot.web.dto.Match map(Match match);

    List<net.andresbustamante.yafoot.web.dto.Match> map(List<Match> matches);
}

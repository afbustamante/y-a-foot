package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.config.SpringMapperConfig;
import net.andresbustamante.yafoot.model.Match;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = SpringMapperConfig.class, uses = {CarMapper.class, PlayerMapper.class, SiteMapper.class, StringMapper.class,
        RegistrationMapper.class})
public interface MatchMapper {

    @Mapping(source = "author", target = "creator")
    @Mapping(source = "sharingEnabled", target = "codeSharingEnabled")
    @Mapping(target = "creationDate", ignore = true)
    Match map(net.andresbustamante.yafoot.web.dto.Match match);

    @Mapping(target = "author", source = "creator")
    @Mapping(target = "sharingEnabled", source = "codeSharingEnabled")
    net.andresbustamante.yafoot.web.dto.Match map(Match match);
}

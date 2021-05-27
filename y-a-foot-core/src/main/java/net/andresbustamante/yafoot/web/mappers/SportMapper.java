package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.web.config.SpringMapperConfig;
import net.andresbustamante.yafoot.web.dto.Sport;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = SpringMapperConfig.class)
public interface SportMapper {

    Sport map(net.andresbustamante.yafoot.core.model.Sport sport);

    List<Sport> map(List<net.andresbustamante.yafoot.core.model.Sport> sports);
}

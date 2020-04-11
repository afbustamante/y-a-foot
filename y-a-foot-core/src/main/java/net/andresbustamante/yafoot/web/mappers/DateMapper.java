package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.config.DtoMapperConfig;
import net.andresbustamante.yafoot.util.DateUtils;
import org.mapstruct.Mapper;

import java.time.ZonedDateTime;

@Mapper(config = DtoMapperConfig.class)
public interface DateMapper {

    default String map(ZonedDateTime dateTime) {
        return DateUtils.format(dateTime);
    }
}

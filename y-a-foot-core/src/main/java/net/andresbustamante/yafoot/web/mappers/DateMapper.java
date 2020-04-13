package net.andresbustamante.yafoot.web.mappers;

import net.andresbustamante.yafoot.config.SpringMapperConfig;
import org.mapstruct.Mapper;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

@Mapper(config = SpringMapperConfig.class)
public interface DateMapper {

    default OffsetDateTime map(ZonedDateTime dateTime) {
        return (dateTime != null) ? dateTime.toOffsetDateTime() : null;
    }

    default ZonedDateTime map(OffsetDateTime dateTime) {
        return (dateTime != null) ? dateTime.toZonedDateTime() : null;
    }
}

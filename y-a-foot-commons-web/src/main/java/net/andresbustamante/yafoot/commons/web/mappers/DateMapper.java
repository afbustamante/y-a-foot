package net.andresbustamante.yafoot.commons.web.mappers;

import net.andresbustamante.yafoot.commons.util.DateUtils;
import net.andresbustamante.yafoot.commons.web.config.SpringMapperConfig;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * Dates and date-times mapper.
 */
@Mapper(config = SpringMapperConfig.class)
public interface DateMapper {

    /**
     * Maps a date-time from OffsetDateTime to LocalDateTime.
     *
     * @param dateTime Date-time to map
     * @return Resulting date-time in LocalDateTime
     */
    default LocalDateTime map(OffsetDateTime dateTime) {
        return DateUtils.toLocalDateTime(dateTime);
    }

    /**
     * Maps a date-time from LocalDateTime to OffsetDateTime.
     *
     * @param dateTime Date-time to map
     * @return Resulting date-time in OffsetDateTime
     */
    default OffsetDateTime map(LocalDateTime dateTime) {
        return DateUtils.toOffsetDateTime(dateTime);
    }
}

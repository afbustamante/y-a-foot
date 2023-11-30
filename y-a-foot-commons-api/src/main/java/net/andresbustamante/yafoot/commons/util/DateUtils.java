package net.andresbustamante.yafoot.commons.util;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

/**
 * Date transformation utilities.
 *
 * @author andresbustamante
 */
public final class DateUtils {

    private DateUtils() {
        // no-op
    }

    /**
     * Transforms an old Date object into a LocalDateTime object.
     *
     * @param date Date to transform
     * @return LocalDateTime equivalent date
     */
    public static LocalDateTime toLocalDateTime(final Date date) {
        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();

        return LocalDateTime.ofInstant(date.toInstant(), calendar.getTimeZone().toZoneId());
    }

    /**
     * Transforms a date-time from OffsetDateTime to LocalDateTime.
     *
     * @param dateTime Date-time to transform
     * @return Resulting LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(final OffsetDateTime dateTime) {
        return dateTime != null ? dateTime.atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime() : null;
    }

    /**
     * Transforms a date-time from LocalDateTime to OffsetDateTime using the default timezone configured for Spring.
     *
     * @param dateTime Date-time to transform
     * @return Resulting OffsetDateTime
     */
    public static OffsetDateTime toOffsetDateTime(final LocalDateTime dateTime) {
        return dateTime != null ? dateTime.atZone(ZoneId.systemDefault()).toOffsetDateTime() : null;
    }
}

package net.andresbustamante.yafoot.commons.util;

import java.time.LocalDateTime;
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
    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();

        return LocalDateTime.ofInstant(date.toInstant(), calendar.getTimeZone().toZoneId());
    }
}

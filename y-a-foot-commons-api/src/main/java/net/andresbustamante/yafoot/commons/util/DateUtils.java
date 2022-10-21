package net.andresbustamante.yafoot.commons.util;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

/**
 * @author andresbustamante
 */
public final class DateUtils {

    private DateUtils() {
        // no-op
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();

        return LocalDateTime.ofInstant(date.toInstant(), calendar.getTimeZone().toZoneId());
    }
}

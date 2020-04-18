package net.andresbustamante.yafoot.util;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import static net.andresbustamante.yafoot.util.LocaleUtils.ENGLISH;
import static net.andresbustamante.yafoot.util.LocaleUtils.SPANISH;
import static net.andresbustamante.yafoot.util.LocaleUtils.FRENCH;

/**
 * @author andresbustamante
 */
public class DateUtils {

    private DateUtils() {}

    public static String getDateTimePattern(String language) {
        if (language == null) {
            return "yyyy-MM-dd H:mm";
        }

        switch (language) {
            case SPANISH:
                return "dd/MM/yyyy h:mm a";
            case FRENCH:
                return "dd/MM/yyyy H:mm";
            case ENGLISH:
                return "yyyy-MM-dd h:mm a";
            default:
                return "yyyy-MM-dd H:mm";
        }
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();

        return LocalDateTime.ofInstant(date.toInstant(), calendar.getTimeZone().toZoneId());
    }
}

package net.andresbustamante.yafoot.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import static net.andresbustamante.yafoot.util.LocaleUtils.ENGLISH;
import static net.andresbustamante.yafoot.util.LocaleUtils.SPANISH;
import static net.andresbustamante.yafoot.util.LocaleUtils.FRENCH;

/**
 * @author andresbustamante
 */
public class DateUtils {

    public static final String DATE_STYLE = "date";
    public static final String DATE_TIME_STYLE = "date-time";
    private static final String DD_MM_YYYY = "dd/MM/yyyy";
    private static final String RFC_3339_DATE = "yyyy-MM-dd";
    private static final String RFC_3339_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ssXXX";

    private DateUtils() {}

    public static String format(Date date, String style) {
        if (date == null) {
            return null;
        }
        DateFormat dateFormat = (DATE_STYLE.equals(style)) ? new SimpleDateFormat(DD_MM_YYYY) : new SimpleDateFormat(RFC_3339_DATE_TIME);
        return dateFormat.format(date);
    }

    public static String format(ZonedDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(RFC_3339_DATE_TIME);
        return formatter.format(dateTime);
    }

    public static String format(LocalDate date) {
        if (date == null) {
            return null;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(RFC_3339_DATE);
        return date.format(formatter);
    }

    public static Date parse(String texte) {
        try {
            DateFormat formatDate = new SimpleDateFormat(DD_MM_YYYY);
            return (texte != null) ? formatDate.parse(texte) : null;
        } catch (ParseException e) {
            return null;
        }
    }

    public static ZonedDateTime toZonedDateTime(String text) {
        if (text == null) {
            return null;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(RFC_3339_DATE_TIME);
        return ZonedDateTime.from(formatter.parse(text));
    }

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

    public static Date firstTimeOfDay(Date date) {
        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();

        LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), calendar.getTimeZone().toZoneId()).
                withHour(0).withMinute(0).withSecond(0);

        return Date.from(dateTime.atZone(calendar.getTimeZone().toZoneId()).toInstant());
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();

        return LocalDateTime.ofInstant(date.toInstant(), calendar.getTimeZone().toZoneId());
    }
}

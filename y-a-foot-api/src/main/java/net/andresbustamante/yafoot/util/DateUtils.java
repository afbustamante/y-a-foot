package net.andresbustamante.yafoot.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import static net.andresbustamante.yafoot.util.LocaleUtils.*;

/**
 * @author andresbustamante
 */
public class DateUtils {

    public static final String SEPARATEUR_HEURE = ":";

    private static final String FORMAT_DATE = "dd/MM/yyyy";

    private DateUtils() {}

    public static String formater(Date date) {
        DateFormat formatDate = new SimpleDateFormat(FORMAT_DATE);
        return (date != null) ? formatDate.format(date) : "";
    }

    public static Date transformer(String texte) {
        try {
            DateFormat formatDate = new SimpleDateFormat(FORMAT_DATE);
            return (texte != null) ? formatDate.parse(texte) : null;
        } catch (ParseException e) {
            return null;
        }
    }

    public static String getPatternDate(String langue) {
        if (langue == null) {
            return "yyyy-MM-dd";
        }

        switch (langue) {
            case ESPAGNOL:
            case FRANCAIS:
                return "dd/MM/yyyy";
            case ANGLAIS:
                return "yyyy-MM-dd";
            default:
                return "yyyy-MM-dd";
        }
    }

    public static String getPatternDateHeure(String langue) {
        if (langue == null) {
            return "yyyy/MM/dd H:mm";
        }

        switch (langue) {
            case ESPAGNOL:
                return "dd/MM/yyyy h:mm a";
            case FRANCAIS:
                return "dd/MM/yyyy H:mm";
            case ANGLAIS:
                return "yyyy-MM-dd h:mm a";
            default:
                return "yyyy-MM-dd H:mm";
        }
    }

    public static Date premiereMinuteDuJour(Date date) {
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

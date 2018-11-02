package net.andresbustamante.yafoot.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

    private static final Log log = LogFactory.getLog(DateUtils.class);

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
            return "yyyy/MM/dd";
        }

        switch (langue) {
            case ESPAGNOL:
            case FRANCAIS:
                return "dd/MM/yyyy";
            case ANGLAIS:
                return "yyyy-MM-dd";
            default:
                return "yyyy/MM/dd";
        }
    }

    public static String getPatternHeure(String langue) {
        if (langue == null) {
            return "H:mm";
        }

        switch (langue) {
            case ESPAGNOL:
            case FRANCAIS:
                return "H:mm";
            case ANGLAIS:
                return "h:mm a";
            default:
                return "H:mm";
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
}

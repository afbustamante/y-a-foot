package net.andresbustamante.yafoot.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author andresbustamante
 */
public class DateUtils {

    public static final String FORMAT_DATE = "dd/MM/yyyy";

    private static final Log log = LogFactory.getLog(DateUtils.class);

    private static final DateFormat formatDate = new SimpleDateFormat(FORMAT_DATE);

    public static XMLGregorianCalendar transformer(Date date) {
        if (date == null) {
            return null;
        }

        try {
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(date);

            return DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
        } catch (DatatypeConfigurationException e) {
            log.error("Erreur de configuration au moment de transformer une date", e);
        }
        return null;
    }

    public static Date transformer(XMLGregorianCalendar date) {
        if (date == null) {
            return null;
        }

        return date.toGregorianCalendar().getTime();
    }

    public static String formater(Date date) {
        return (date != null) ? formatDate.format(date) : "";
    }

    public static Date transformer(String texte) {
        try {
            return (texte != null) ? formatDate.parse(texte) : null;
        } catch (ParseException e) {
            return null;
        }
    }
}

package net.andresbustamante.yafoot.util;

import org.joda.time.DateTime;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * @author andresbustamante
 */
public class DateUtilsTest {

    private static final String TEXTE_DATE = "22/04/2017";
    private static final String TEXTE_DATE_INVALIDE = "22.04.2017";

    @Test
    public void testTransformerTexte() throws Exception {
        // Transformer une date valide
        Date date = DateUtils.transformer(TEXTE_DATE);
        assertNotNull(date);
        DateTime dateTime = new DateTime(date);
        assertEquals(2017, dateTime.getYear());
        assertEquals(4, dateTime.getMonthOfYear());
        assertEquals(22, dateTime.getDayOfMonth());

        // Transformer une date invalide
        date = DateUtils.transformer(TEXTE_DATE_INVALIDE);
        assertNull(date);
    }

    @Test
    public void testFormater() throws Exception {
        // Formater une date valide
        DateTime dateTime = new DateTime(2017, 4, 22, 12, 0); // 2017-04-22 12:00
        assertEquals(TEXTE_DATE, DateUtils.formater(dateTime.toDate()));

        // Formater une date invalide
        assertEquals("", DateUtils.formater(null));
    }

    @Test
    public void testPremiereMinuteDuJour() throws Exception {
        DateTime dateTime = DateTime.now();
        DateTime premiereMinute = new DateTime(DateUtils.premiereMinuteDuJour(dateTime.toDate()));
        assertEquals(dateTime.getYear(), premiereMinute.getYear());
        assertEquals(dateTime.getMonthOfYear(), premiereMinute.getMonthOfYear());
        assertEquals(dateTime.getDayOfMonth(), premiereMinute.getDayOfMonth());
        assertEquals(0, premiereMinute.getHourOfDay());
        assertEquals(0, premiereMinute.getMinuteOfDay());
        assertEquals(0, premiereMinute.getSecondOfDay());
        assertEquals(0, premiereMinute.getMillisOfSecond());
        assertNotEquals(dateTime.getMillisOfSecond(), premiereMinute.getMillisOfSecond());
    }

}
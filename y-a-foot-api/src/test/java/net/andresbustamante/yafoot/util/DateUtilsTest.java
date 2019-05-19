package net.andresbustamante.yafoot.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author andresbustamante
 */
class DateUtilsTest {

    private static final String TEXTE_DATE = "22/04/2017";
    private static final String TEXTE_DATE_INVALIDE = "22.04.2017";
    private static final int MONTHS_OFFSET = 1;

    @Test
    void testTransformerTexte() throws Exception {
        // Transformer une date valide
        Date date = DateUtils.transformer(TEXTE_DATE);
        assertNotNull(date);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        assertEquals(2017, calendar.get(Calendar.YEAR));
        assertEquals(Calendar.APRIL, calendar.get(Calendar.MONTH));
        assertEquals(22, calendar.get(Calendar.DAY_OF_MONTH));

        // Transformer une date invalide
        date = DateUtils.transformer(TEXTE_DATE_INVALIDE);
        assertNull(date);
    }

    @Test
    void testFormater() throws Exception {
        // Formater une date valide
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, Calendar.APRIL, 22, 12, 0); // 2017-04-22 12:00
        assertEquals(TEXTE_DATE, DateUtils.formater(calendar.getTime()));

        // Formater une date invalide
        assertEquals("", DateUtils.formater(null));
    }

    @Test
    void testPremiereMinuteDuJour() throws Exception {
        Calendar calendar = Calendar.getInstance();

        LocalDateTime premiereMinute = LocalDateTime.ofInstant(DateUtils.premiereMinuteDuJour(calendar.getTime()).toInstant(),
                calendar.getTimeZone().toZoneId());
        assertEquals(calendar.get(Calendar.YEAR), premiereMinute.getYear());
        assertEquals(calendar.get(Calendar.MONTH) + MONTHS_OFFSET, premiereMinute.getMonth().getValue());
        assertEquals(calendar.get(Calendar.DAY_OF_MONTH), premiereMinute.getDayOfMonth());
        assertEquals(0, premiereMinute.getHour());
        assertEquals(0, premiereMinute.getMinute());
        assertEquals(0, premiereMinute.getSecond());
    }

    @Test
    void testGetPatternDateFrancais() {
        String pattern = DateUtils.getPatternDate(LocaleUtils.FRANCAIS);
        assertNotNull(pattern);
        assertEquals("dd/MM/yyyy", pattern);
    }

    @Test
    void testGetPatternDateAnglais() {
        String pattern = DateUtils.getPatternDate(LocaleUtils.ANGLAIS);
        assertNotNull(pattern);
        assertEquals("yyyy-MM-dd", pattern);
    }

    @Test
    void testGetPatternDateEspagnol() {
        String pattern = DateUtils.getPatternDate(LocaleUtils.ESPAGNOL);
        assertNotNull(pattern);
        assertEquals("dd/MM/yyyy", pattern);
    }

    @Test
    void testGetPatternDateAutre() {
        String pattern = DateUtils.getPatternDate("xx");
        assertNotNull(pattern);
        assertEquals("yyyy-MM-dd", pattern);

        pattern = DateUtils.getPatternDate(null);
        assertNotNull(pattern);
        assertEquals("yyyy-MM-dd", pattern);
    }

    @Test
    void testGetPatternDateHeureFrancais() {
        String pattern = DateUtils.getPatternDateHeure(LocaleUtils.FRANCAIS);
        assertNotNull(pattern);
        assertEquals("dd/MM/yyyy H:mm", pattern);
    }

    @Test
    void testGetPatternDateHeureAnglais() {
        String pattern = DateUtils.getPatternDateHeure(LocaleUtils.ANGLAIS);
        assertNotNull(pattern);
        assertEquals("yyyy-MM-dd h:mm a", pattern);
    }

    @Test
    void testGetPatternDateHeureEspagnol() {
        String pattern = DateUtils.getPatternDateHeure(LocaleUtils.ESPAGNOL);
        assertNotNull(pattern);
        assertEquals("dd/MM/yyyy h:mm a", pattern);
    }

    @Test
    void testGetPatternDateHeureAutre() {
        String pattern = DateUtils.getPatternDateHeure("xx");
        assertNotNull(pattern);
        assertEquals("yyyy-MM-dd H:mm", pattern);

        pattern = DateUtils.getPatternDateHeure(null);
        assertNotNull(pattern);
        assertEquals("yyyy-MM-dd H:mm", pattern);
    }

    @Test
    void testToLocalDateTime() {
        Calendar cal = Calendar.getInstance();

        LocalDateTime dateTime = DateUtils.toLocalDateTime(cal.getTime());
        assertNotNull(dateTime);
        assertEquals(cal.get(Calendar.YEAR), dateTime.getYear());
        assertEquals(cal.get(Calendar.MONTH) + 1, dateTime.getMonth().getValue());
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), dateTime.getDayOfMonth());
        assertEquals(cal.get(Calendar.HOUR_OF_DAY), dateTime.getHour());
        assertEquals(cal.get(Calendar.MINUTE), dateTime.getMinute());
        assertEquals(cal.get(Calendar.SECOND), dateTime.getSecond());
    }
}
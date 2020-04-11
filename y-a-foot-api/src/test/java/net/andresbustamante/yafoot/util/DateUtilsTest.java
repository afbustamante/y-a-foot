package net.andresbustamante.yafoot.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author andresbustamante
 */
class DateUtilsTest {

    private static final String TEXTE_DATE = "22/04/2017";
    private static final String TEXTE_DATE_INVALIDE = "22.04.2017";
    private static final int MONTHS_OFFSET = 1;

    @Test
    void testDateParsing() throws Exception {
        // Transformer une date valide
        Date date = DateUtils.parse(TEXTE_DATE);
        assertNotNull(date);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        assertEquals(2017, calendar.get(Calendar.YEAR));
        assertEquals(Calendar.APRIL, calendar.get(Calendar.MONTH));
        assertEquals(22, calendar.get(Calendar.DAY_OF_MONTH));

        // Transformer une date invalide
        date = DateUtils.parse(TEXTE_DATE_INVALIDE);
        assertNull(date);
    }

    @Test
    void testDateFormat_dateTimeStyle() throws Exception {
        // Given
        TimeZone tz = TimeZone.getTimeZone("CET");
        long timestamp = 1492862400000L; // 2017-04-22 12:00:00 UTC
        ZoneOffset offset = ZoneOffset.ofTotalSeconds(tz.getOffset(timestamp) / 1000);

        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, Calendar.APRIL, 22, 12, 0, 0); // 2017-04-22 12:00:00
        calendar.setTimeZone(tz);

        // When
        String actual = DateUtils.format(calendar.getTime(), DateUtils.DATE_TIME_STYLE);

        // Then
        assertEquals("2017-04-22T12:00:00" + offset, actual);
    }

    @Test
    void testDateFormat_dateStyle() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, Calendar.APRIL, 22); // 2017-04-22
        assertEquals(TEXTE_DATE, DateUtils.format(calendar.getTime(), DateUtils.DATE_STYLE));
    }

    @Test
    void testFirstTimeOfDay() throws Exception {
        Calendar calendar = Calendar.getInstance();

        LocalDateTime premiereMinute = LocalDateTime.ofInstant(DateUtils.firstTimeOfDay(calendar.getTime()).toInstant(),
                calendar.getTimeZone().toZoneId());
        assertEquals(calendar.get(Calendar.YEAR), premiereMinute.getYear());
        assertEquals(calendar.get(Calendar.MONTH) + MONTHS_OFFSET, premiereMinute.getMonth().getValue());
        assertEquals(calendar.get(Calendar.DAY_OF_MONTH), premiereMinute.getDayOfMonth());
        assertEquals(0, premiereMinute.getHour());
        assertEquals(0, premiereMinute.getMinute());
        assertEquals(0, premiereMinute.getSecond());
    }

    @Test
    void testGetPatternDateHeureFrancais() {
        String pattern = DateUtils.getDateTimePattern(LocaleUtils.FRENCH);
        assertNotNull(pattern);
        assertEquals("dd/MM/yyyy H:mm", pattern);
    }

    @Test
    void testGetPatternDateHeureAnglais() {
        String pattern = DateUtils.getDateTimePattern(LocaleUtils.ENGLISH);
        assertNotNull(pattern);
        assertEquals("yyyy-MM-dd h:mm a", pattern);
    }

    @Test
    void testGetPatternDateHeureEspagnol() {
        String pattern = DateUtils.getDateTimePattern(LocaleUtils.SPANISH);
        assertNotNull(pattern);
        assertEquals("dd/MM/yyyy h:mm a", pattern);
    }

    @Test
    void testGetPatternDateHeureAutre() {
        String pattern = DateUtils.getDateTimePattern("xx");
        assertNotNull(pattern);
        assertEquals("yyyy-MM-dd H:mm", pattern);

        pattern = DateUtils.getDateTimePattern(null);
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

    @Test
    void testZonedDateTimeParsing() {
        String text = "1996-12-19T16:39:57-08:00";
        ZonedDateTime expectedDateTime = ZonedDateTime.of(1996, 12, 19, 16, 39, 57, 0, ZoneOffset.of("-08:00"));

        ZonedDateTime dateTime = DateUtils.toZonedDateTime(text);

        assertNotNull(dateTime);
        assertEquals(expectedDateTime, dateTime);
    }

    @Test
    void testZonedDateTimeFormat() {
        ZonedDateTime dateTime = ZonedDateTime.of(1996, 12, 19, 16, 39, 57, 0, ZoneOffset.of("-08:00"));
        String expectedText = "1996-12-19T16:39:57-08:00";

        String text = DateUtils.format(dateTime);

        assertNotNull(text);
        assertEquals(expectedText, text);
    }

    @Test
    void testLocalDateFormat() {
        LocalDate localDate = LocalDate.of(1996, 12, 19);
        String expectedText = "1996-12-19";

        String text = DateUtils.format(localDate);

        assertNotNull(text);
        assertEquals(expectedText, text);
    }
}
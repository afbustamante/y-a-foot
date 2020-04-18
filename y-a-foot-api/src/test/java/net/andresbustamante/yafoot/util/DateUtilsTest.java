package net.andresbustamante.yafoot.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author andresbustamante
 */
class DateUtilsTest {

    @Test
    void testGetDateTimePatternFrench() {
        String pattern = DateUtils.getDateTimePattern(LocaleUtils.FRENCH);
        assertNotNull(pattern);
        assertEquals("dd/MM/yyyy H:mm", pattern);
    }

    @Test
    void testGetDateTimePatternEnglish() {
        String pattern = DateUtils.getDateTimePattern(LocaleUtils.ENGLISH);
        assertNotNull(pattern);
        assertEquals("yyyy-MM-dd h:mm a", pattern);
    }

    @Test
    void testGetDateTimePatternSpanish() {
        String pattern = DateUtils.getDateTimePattern(LocaleUtils.SPANISH);
        assertNotNull(pattern);
        assertEquals("dd/MM/yyyy h:mm a", pattern);
    }

    @Test
    void testGetDateTimePatternOther() {
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
}
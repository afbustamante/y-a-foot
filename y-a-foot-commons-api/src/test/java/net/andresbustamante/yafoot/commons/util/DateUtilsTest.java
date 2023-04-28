package net.andresbustamante.yafoot.commons.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author andresbustamante
 */
class DateUtilsTest {

    @BeforeEach
    void setUp() {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Paris"));
    }

    @Test
    void testDateToLocalDateTime() {
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
    void testOffsetDateTimeToLocalDateTime() {
        // Given
        OffsetDateTime odt = OffsetDateTime.parse("2023-04-24T12:34:56.000Z");

        // When
        LocalDateTime dateTime = DateUtils.toLocalDateTime(odt);

        // Then
        assertNotNull(dateTime);
        assertEquals(2023, dateTime.getYear());
        assertEquals(4, dateTime.getMonth().getValue());
        assertEquals(24, dateTime.getDayOfMonth());
        assertEquals(14, dateTime.getHour());
        assertEquals(34, dateTime.getMinute());
        assertEquals(56, dateTime.getSecond());
    }

    @Test
    void testLocalDateTimeToOffsetDateTime() {
        // Given
        LocalDateTime ldt = LocalDateTime.parse("2023-04-24T12:34:56");

        // When
        OffsetDateTime dateTime = DateUtils.toOffsetDateTime(ldt);

        // Then
        assertNotNull(dateTime);
        assertEquals(2023, dateTime.getYear());
        assertEquals(4, dateTime.getMonth().getValue());
        assertEquals(24, dateTime.getDayOfMonth());
        assertEquals(12, dateTime.getHour());
        assertEquals(34, dateTime.getMinute());
        assertEquals(56, dateTime.getSecond());
        assertEquals(ZoneOffset.ofHours(2), dateTime.getOffset());
        assertEquals("2023-04-24T12:34:56+02:00", dateTime.toString());
    }
}

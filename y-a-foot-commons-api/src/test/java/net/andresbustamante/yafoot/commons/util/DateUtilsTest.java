package net.andresbustamante.yafoot.commons.util;

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

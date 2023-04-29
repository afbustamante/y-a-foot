package net.andresbustamante.yafoot.core.web.mappers;

import net.andresbustamante.yafoot.core.model.Sport;
import net.andresbustamante.yafoot.core.model.enums.SportEnum;
import net.andresbustamante.yafoot.web.dto.SportCode;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SportMapperTest {

    private final SportMapper sportMapper = new SportMapper() {
        @Override
        public net.andresbustamante.yafoot.web.dto.Sport map(Sport sport) {
            return null;
        }

        @Override
        public List<net.andresbustamante.yafoot.web.dto.Sport> map(List<Sport> sports) {
            return null;
        }

        @Override
        public SportCode map(SportEnum value) {
            return null;
        }

        @Override
        public SportEnum map(SportCode code) {
            return null;
        }
    };

    @Test
    void testMapSportCodeOK() {
        // Given
        String code = "TABLE_TENNIS";

        // When
        var result = sportMapper.mapSportCode(code);

        // Then
        assertNotNull(result);
        assertEquals(SportCode.TABLE_TENNIS, result);
    }

    @Test
    void testMapSportEmptyCode() {
        // When
        var result = sportMapper.mapSportCode(null);

        // Then
        assertNull(result);
    }
}

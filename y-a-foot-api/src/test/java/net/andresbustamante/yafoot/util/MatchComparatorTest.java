package net.andresbustamante.yafoot.util;

import net.andresbustamante.yafoot.model.xs.Match;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

class MatchComparatorTest {

    private static int INFERIEUR = -1;
    private static int SUPERIEUR = 1;
    private static int EGAL = 0;

    private MatchComparator comparator;

    @BeforeEach
    void setUp() {
        comparator = new MatchComparator();
    }

    @Test
    void comparerParDate() {
        // Given
        Calendar now = Calendar.getInstance();
        Match match1 = new Match();
        match1.setId(1);
        match1.setDate(now);

        Calendar later = Calendar.getInstance();
        later.add(Calendar.HOUR, 2);
        Match match2 = new Match();
        match2.setId(2);
        match2.setDate(later);

        // When
        int c = comparator.compare(match2, match1);

        // Then
        assertEquals(SUPERIEUR, c);
    }

    @Test
    void comparerParId() {
        // Given
        Calendar now = Calendar.getInstance();
        Match match1 = new Match();
        match1.setId(1);
        match1.setDate(now);
        Match match2 = new Match();
        match2.setId(2);
        match2.setDate(now);

        // When
        int c = comparator.compare(match1, match2);

        // Then
        assertEquals(INFERIEUR, c);
    }

    @Test
    void comparerMatchsEgaux() {
        // Given
        Calendar now = Calendar.getInstance();
        Match match1 = new Match();
        match1.setId(1);
        match1.setDate(now);
        Match match2 = new Match();
        match2.setId(1);
        match2.setDate(now);

        // When
        int c = comparator.compare(match1, match2);

        // Then
        assertEquals(EGAL, c);
    }
}
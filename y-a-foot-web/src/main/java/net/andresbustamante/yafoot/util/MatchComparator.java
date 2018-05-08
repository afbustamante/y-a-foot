package net.andresbustamante.yafoot.util;

import net.andresbustamante.yafoot.model.Match;

import java.util.Comparator;

/**
 * @author andresbustamante
 */
public class MatchComparator implements Comparator<Match> {

    @Override
    public int compare(Match match1, Match match2) {
        if (match1 == match2) {
            return 0;
        } else {
            if (!match1.getDateMatch().equals(match2.getDateMatch())) {
                return match1.getDateMatch().compareTo(match2.getDateMatch());
            } else if (match1.getId() != null && match2.getId() != null) {
                return match1.getId().compareTo(match2.getId());
            } else {
                return 0;
            }
        }
    }
}

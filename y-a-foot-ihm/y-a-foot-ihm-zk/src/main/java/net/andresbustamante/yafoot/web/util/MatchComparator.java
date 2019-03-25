package net.andresbustamante.yafoot.web.util;

import net.andresbustamante.yafoot.model.xs.Match;

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
            if (!match1.getDate().equals(match2.getDate())) {
                return match1.getDate().compareTo(match2.getDate());
            } else if (match1.getId() != null && match2.getId() != null) {
                return match1.getId().compareTo(match2.getId());
            } else {
                return 0;
            }
        }
    }
}

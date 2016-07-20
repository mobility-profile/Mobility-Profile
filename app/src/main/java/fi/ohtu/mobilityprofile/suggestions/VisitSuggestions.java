package fi.ohtu.mobilityprofile.suggestions;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import fi.ohtu.mobilityprofile.Util;
import fi.ohtu.mobilityprofile.data.VisitDao;
import fi.ohtu.mobilityprofile.domain.Visit;

/**
 * This class creates suggestions based on data collected about user's movement and visited places.
 */
public class VisitSuggestions implements SuggestionSource {
    private VisitDao visitDao;

    /**
     * Creates the VisitSuggestions.
     *
     * @param visitDao DAO for visits user has made
     */
    public VisitSuggestions(VisitDao visitDao) {
        this.visitDao = visitDao;
    }

    /**
     * Returns a list of probable locations the user could want to visit based on the data collected
     * from previous visits. A visit is considered valid if it was max 1 hour earlier or max 3 hours
     * later that the current time.
     *
     * @return List of probable destinations
     */
    @Override
    public List<Suggestion> getSuggestions(String startLocation) {
        List<Suggestion> suggestions = new ArrayList<>();

        for (Visit visit : visitDao.getAllVisits()) {
            if (Util.aroundTheSameTime(new Time(visit.getTimestamp()), 1, 3)) {
                Suggestion suggestion = new Suggestion(visit.getOriginalLocation(), SuggestionAccuracy.HIGH, VISIT_SUGGESTION);
                suggestions.add(suggestion);
                // This returns location with coordinates : "Liisankatu 1, Helsinki, Finland!00.0000!00.0000!"
            }
        }

        return suggestions;
    }
}

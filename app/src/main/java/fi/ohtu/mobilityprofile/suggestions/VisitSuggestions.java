package fi.ohtu.mobilityprofile.suggestions;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fi.ohtu.mobilityprofile.Util;
import fi.ohtu.mobilityprofile.data.VisitDao;
import fi.ohtu.mobilityprofile.domain.Visit;

public class VisitSuggestions implements SuggestionSource {
    private VisitDao visitDao;

    public VisitSuggestions(VisitDao visitDao) {
        this.visitDao = visitDao;
    }

    /**
     * Selects destination based on previous visits.
     * Checks if the user has visited some location around the same time in the past,
     * max 1 hour earlier or max 3 hours later than current time.
     * Searches from visits.
     *
     * @return Previously visited place
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

package fi.ohtu.mobilityprofile.suggestions.locationHistory;

import java.util.ArrayList;
import java.util.List;

import fi.ohtu.mobilityprofile.data.SignificantPlaceDao;
import fi.ohtu.mobilityprofile.data.VisitDao;
import fi.ohtu.mobilityprofile.domain.SignificantPlace;
import fi.ohtu.mobilityprofile.domain.Visit;
import fi.ohtu.mobilityprofile.suggestions.Suggestion;
import fi.ohtu.mobilityprofile.suggestions.SuggestionAccuracy;
import fi.ohtu.mobilityprofile.suggestions.SuggestionSource;

/**
 * This class creates suggestions based on the user's visits to places he has visited frequently in the past.
 */
public class VisitSuggestions implements SuggestionSource {
    private SignificantPlaceDao significantPlaceDao;
    private VisitDao visitdao;
    private List<Suggestion> suggestions;

    public VisitSuggestions(SignificantPlaceDao dao, VisitDao visitdao) {
        this.significantPlaceDao = dao;
        this.visitdao = visitdao;
    }

    /**
     * Returns suggestions based on visits to SignificantPlaces.
     *
     * @param startLocation Starting location
     * @return List of probable destinations
     */
    @Override
    public List<Suggestion> getSuggestions(String startLocation) {
        suggestions = new ArrayList<>();

        SignificantPlace significantPlace = significantPlaceDao.getSignificantPlaceIfCurrentLocationIsOne(startLocation, 100);
        if (significantPlace != null) {
            calculateNextDestination(significantPlace);
        }

        return suggestions;
    }

    /**
     * Calculates next destination based on visited SignificantPlaces in the past.
     * @param startLocation
     */
    private void calculateNextDestination(SignificantPlace startLocation) {
        List<Visit> visits = visitdao.getAllVisitsToSignificantPlaces();

        // number of visits must at least four so it can provide next, current, previous and before previous locations.
        if (visits.size() >= 4) {
            String previousLocation = visits.get(0).getSignificantPlace().getLocation();
            String beforePrevious = visits.get(1).getSignificantPlace().getLocation();

            // first and two last items are ignored because they do not have either next or previous and before previous location
            for (int i = 1; i < visits.size() - 2; i++) {

                // checks if startLocation is the same as the location currently examined in the list
                if (visits.get(i).getSignificantPlace().getLocation().equals(startLocation)) {

                    // checks if the previous location in the past is the same as previous location from the current location
                    // and before previous location in the past is the same as before previous location from the current location
                    if ((visits.get(i + 1).getSignificantPlace().getLocation().equals(previousLocation))
                            && (visits.get(i + 2).getSignificantPlace().getLocation().equals(beforePrevious))) {
                        suggestions.add(new Suggestion(visits.get(i - 1).getSignificantPlace().getLocation(), SuggestionAccuracy.HIGH, VISIT_SUGGESTIONS));
                    }

                }
            }
        }
    }
}

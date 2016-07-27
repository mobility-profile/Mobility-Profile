package fi.ohtu.mobilityprofile.suggestions.locationHistory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Map<String, Integer> nextDestinations;

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

        SignificantPlace significantPlace = significantPlaceDao.getSignificantPlaceIfCurrentLocationIsOne(startLocation, 50);
        if (significantPlace != null) {
            calculateNextDestinations(significantPlace);
            calculateSuggestions();
        }

        return suggestions;
    }

    /**
     * Calculates next destination based on visited SignificantPlaces in the past.
     * @param startLocation starting location
     */
    private void calculateNextDestinations(SignificantPlace startLocation) {
        List<Visit> visits = visitdao.getAllVisitsToSignificantPlaces();

        // number of visits must be more than four so it can provide next, current, previous and before previous locations.
        if (visits.size() <= 4) {
            return;
        }

        nextDestinations = new HashMap<>();
        String previousLocation = visits.get(1).getLocation();
        String beforePrevious = visits.get(2).getLocation();

        // first and two last items are ignored because they do not have either next or previous and before previous location
        for (int i = 1; i < visits.size() - 2; i++) {

            // checks if startLocation is the same as the location currently examined in the list
            if (visits.get(i).getLocation().equals(startLocation)) {

                // checks if the previous location in the past is the same as previous location from the current location
                // and before previous location in the past is the same as before previous location from the current location
                if ((visits.get(i + 1).getLocation().equals(previousLocation))
                        && (visits.get(i + 2).getLocation().equals(beforePrevious))) {
                    addDestinationToHashMap(visits.get(i - 1).getLocation());
                }
             }
        }
    }

    /**
     * Adds a possible next destination to hashMap and increments the number of visits to that place from
     * current location by one.
     * @param nextDestination possible next destination
     */
    private void addDestinationToHashMap(String nextDestination) {
        int count = nextDestinations.containsKey(nextDestination) ? nextDestinations.get(nextDestination) : 0;
        nextDestinations.put(nextDestination, count + 1);
    }

    /**
     * Adds destinations to suggestions list that have the highest likelihood of visiting them next from current location.
     */
    private void calculateSuggestions() {
        suggestions = new ArrayList<>();

        int maxValue = Collections.max(nextDestinations.values());
        for (Map.Entry<String, Integer> entry : nextDestinations.entrySet()) {
            if (entry.getValue() == maxValue) {
                suggestions.add(new Suggestion(entry.getKey(), SuggestionAccuracy.HIGH, VISIT_SUGGESTIONS));
            }
        }
    }
}
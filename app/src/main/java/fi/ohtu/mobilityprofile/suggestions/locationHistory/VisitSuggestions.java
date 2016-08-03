package fi.ohtu.mobilityprofile.suggestions.locationHistory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import fi.ohtu.mobilityprofile.data.SignificantPlaceDao;
import fi.ohtu.mobilityprofile.domain.Visit;

import fi.ohtu.mobilityprofile.data.PlaceDao;
import fi.ohtu.mobilityprofile.data.VisitDao;
import fi.ohtu.mobilityprofile.suggestions.*;

/**
 * This class creates suggestions based on the user's visits to places he has visited frequently in the past.
 */
public class VisitSuggestions implements SuggestionSource {

    private SignificantPlaceDao significantPlaceDao;
    private VisitDao visitdao;
    private List<Suggestion> suggestions;
    private Map<String, Integer> nextDestinations;
    private PlaceDao placeDao;
    List<Visit> visits;

    /**
     * Creates VisitSuggestions.
     * @param dao SignificantPlaceDao
     * @param visitdao VisitDao
     */
    public VisitSuggestions(SignificantPlaceDao dao, VisitDao visitdao) {
        this.significantPlaceDao = dao;
        this.visitdao = visitdao;
        suggestions = new ArrayList<>();
    }

    /**
     * Returns suggestions based on visits to SignificantPlaces.
     *
     * @param startLocation Starting location
     * @return List of probable destinations
     */
    @Override
    public List<Suggestion> getSuggestions(String startLocation) {

//        for (Place place : placeDao.getAll()) {
//            if (Util.aroundTheSameTime(new Time(place.getTimestamp()), 1, 3)) {
//                Suggestion suggestion = new Suggestion(place.getOriginalLocation(), SuggestionAccuracy.HIGH, VISIT_SUGGESTION);
//                suggestions.add(suggestion);
//                // This returns location with coordinates : "Liisankatu 1, Helsinki, Finland!00.0000!00.0000!"
//            }
//        }
        visits = visitdao.getAllVisitsToSignificantPlaces();
        // number of visits must be more than four so it can provide next, current, previous and before previous locations.
        if (visits.size() > 4) {
            calculateNextDestinations(startLocation);
            calculateSuggestions();
        }

        return suggestions;
    }

    /**
     * Calculates next destinations based on visited SignificantPlaces in the past.
     * @param startLocation starting location
     */
    private void calculateNextDestinations(String startLocation) {

        nextDestinations = new HashMap<>();
        String previousLocation = visits.get(1).getAddress();
        String beforePrevious = visits.get(2).getAddress();

        // first and two last items are ignored because they do not have either next or previous and before previous location
        for (int i = 1; i < visits.size() - 2; i++) {

            // checks if startLocation is the same as the location currently examined in the list
            if (visits.get(i).getAddress().equals(startLocation)) {

                // checks if the previous location in the past is the same as previous location from the current location
                // and before previous location in the past is the same as before previous location from the current location
                if ((visits.get(i + 1).getAddress().equals(previousLocation))
                        && (visits.get(i + 2).getAddress().equals(beforePrevious))) {
                    addToNextDestinations(visits.get(i - 1).getAddress());
                }
            }
        }
    }

    /**
     * Adds a possible next destination to hashMap and increments the number of visits to that place from
     * current location by one.
     * @param nextDestination possible next destination
     */
    private void addToNextDestinations(String nextDestination) {
        int count = nextDestinations.containsKey(nextDestination) ? nextDestinations.get(nextDestination) : 0;
        nextDestinations.put(nextDestination, count + 1);
    }

    /**
     * Adds destinations to suggestions list that have the highest likelihood of visiting them next from current location.
     */
    private void calculateSuggestions() {
        if (!nextDestinations.isEmpty()) {
            int maxValue = Collections.max(nextDestinations.values());
            for (Map.Entry<String, Integer> entry : nextDestinations.entrySet()) {
                if (entry.getValue() == maxValue) {
                    suggestions.add(new Suggestion(entry.getKey(), SuggestionAccuracy.HIGH, VISIT_SUGGESTIONS));
                }
            }
        }
    }
}

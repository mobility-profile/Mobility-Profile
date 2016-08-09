package fi.ohtu.mobilityprofile.suggestions.sources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.ohtu.mobilityprofile.domain.GpsPoint;
import fi.ohtu.mobilityprofile.domain.Place;
import fi.ohtu.mobilityprofile.domain.Visit;

import fi.ohtu.mobilityprofile.data.VisitDao;
import fi.ohtu.mobilityprofile.suggestions.*;
import fi.ohtu.mobilityprofile.suggestions.locationHistory.GpsPointClusterizer;

/**
 * This class creates suggestions based on the user's visits to places he has visited frequently in the past.
 */
public class VisitSuggestions implements SuggestionSource {

    private GpsPointClusterizer gpsPointClusterizer;

    /**
     * Creates VisitSuggestions.
     */
    public VisitSuggestions() {
    }

    /**
     * Returns suggestions based on visits to SignificantPlaces.
     *
     * @param startLocation Starting location
     * @return List of probable destinations
     */
    @Override
    public List<Suggestion> getSuggestions(GpsPoint startLocation) {
        Map<Place, Integer> nextDestinations = calculateNextDestinations(startLocation);
        List<Suggestion> suggestions = new ArrayList<>();

        if (!nextDestinations.isEmpty() && userStillAtLastVisitLocation(startLocation, VisitDao.getLast())) {
            int maxValue = Collections.max(nextDestinations.values());
            for (Map.Entry<Place, Integer> entry : nextDestinations.entrySet()) {
                if (entry.getValue() == maxValue) {
                    suggestions.add(new Suggestion(entry.getKey().getAddress(), SuggestionAccuracy.HIGH, VISIT_SUGGESTION, entry.getKey().getCoordinate()));
                }
            }
        }
        return suggestions;
    }

    /**
     * Calculates next destinations based on visited SignificantPlaces in the past.
     *
     * @param startLocation starting location
     */
    private Map<Place, Integer> calculateNextDestinations(GpsPoint startLocation) {

        List<Visit> visits = VisitDao.getAll();
        Map<Place, Integer> nextDestinations = new HashMap<>();
        if (visits.size() > 3) {

            Place currentPlace = VisitDao.getLast().getPlace();
            Place previousLocation = visits.get(1).getPlace();
            Place beforePrevious = visits.get(2).getPlace();

            // first and two last items are ignored because they do not have either next or previous and before previous location
            for (int i = 1; i < visits.size() - 2; i++) {

                // checks if startLocation is the same as the location currently examined in the list
                if (visits.get(i).getPlace().equals(currentPlace)) {

                    // checks if the previous location in the past is the same as previous location from the current location
                    // and before previous location in the past is the same as before previous location from the current location
                    if ((visits.get(i + 1).getPlace().equals(previousLocation))
                            && (visits.get(i + 2).getPlace().equals(beforePrevious))) {

                        addToNextDestinations(visits.get(i - 1).getPlace(), nextDestinations);
                    }
                }
            }
        }
        return nextDestinations;
    }

    private boolean userStillAtLastVisitLocation(GpsPoint startLocation, Visit lastVisit) {
        return Math.abs(startLocation.getTimestamp() - lastVisit.getExitTime()) < gpsPointClusterizer.TIME_SPENT_IN_CLUSTER_THRESHOLD && startLocation.distanceTo(lastVisit) < gpsPointClusterizer.CLUSTER_RADIUS;
    }

    /**
     * Adds a possible next destination to hashMap and increments the number of visits to that place from
     * current location by one.
     *
     * @param nextDestination possible next destination
     */
    private void addToNextDestinations(Place nextDestination, Map<Place, Integer> nextDestinations) {
        int count = nextDestinations.containsKey(nextDestination) ? nextDestinations.get(nextDestination) : 0;
        nextDestinations.put(nextDestination, count + 1);
    }

}
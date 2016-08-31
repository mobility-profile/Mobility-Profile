package fi.ohtu.mobilityprofile.suggestions.sources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.ohtu.mobilityprofile.domain.Place;
import fi.ohtu.mobilityprofile.domain.StartLocation;
import fi.ohtu.mobilityprofile.domain.Visit;

import fi.ohtu.mobilityprofile.data.VisitDao;
import fi.ohtu.mobilityprofile.suggestions.*;
import fi.ohtu.mobilityprofile.suggestions.locationHistory.GpsPointClusterizer;

/**
 * This class creates suggestions based on the user's visits to places he has visited frequently in the past.
 */
public class VisitSuggestions implements SuggestionSource {

    private Map<Place, Integer> nextDestinations;
    private Map<Place, Integer> lowerAccuracyDestinations;

    /**
     * Creates VisitSuggestions.
     */
    public VisitSuggestions() {
        this.nextDestinations = new HashMap<>();
        this.lowerAccuracyDestinations = new HashMap<>();
    }

    /**
     * Returns suggestions based on visits to SignificantPlaces.
     *
     * @param startLocation Starting location
     * @return List of probable destinations
     */
    @Override
    public List<Suggestion> getSuggestions(StartLocation startLocation) {
        calculateNextDestinations(startLocation);
        List<Suggestion> suggestions = new ArrayList<>();

        if (!nextDestinations.isEmpty() && userStillAtLastVisitLocation(startLocation, VisitDao.getLast())) {
            int maxValue = Collections.max(nextDestinations.values());

            for (Map.Entry<Place, Integer> entry : nextDestinations.entrySet()) {
                if (entry.getValue() == maxValue) {
                    suggestions.add(new Suggestion(entry.getKey(), SuggestionAccuracy.HIGH, VISIT_SUGGESTION));
                }
            }
        }

        if (suggestions.size() <= 2 && lowerAccuracyDestinations.size() > 0 && userStillAtLastVisitLocation(startLocation, VisitDao.getLast())) {
            int maxValue = Collections.max(lowerAccuracyDestinations.values());

                for (Map.Entry<Place, Integer> entry : lowerAccuracyDestinations.entrySet()) {
                    if (entry.getValue() == maxValue && !nextDestinations.containsKey(entry.getKey())) {
                        suggestions.add(new Suggestion(entry.getKey(), SuggestionAccuracy.MODERATE, VISIT_SUGGESTION));
                    }
                    if (suggestions.size() >= 3) {
                        break;
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
    private void calculateNextDestinations(StartLocation startLocation) {

        List<Visit> visits = VisitDao.getAll();
        if (visits.size() > 3) {

            Place currentPlace = VisitDao.getLast().getPlace();

            if (currentPlace != null && currentPlace.getCoordinate().distanceTo(startLocation.getCoordinate()) < GpsPointClusterizer.CLUSTER_RADIUS) {

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
                        //if previous location is the same but location before previous location is not,
                        //it is added to list of destinations of lower accuracy.
                        else if (visits.get(i + 1).getPlace().equals(previousLocation)
                                && !nextDestinations.containsKey(visits.get(i - 1).getPlace())) {
                            addToNextDestinations(visits.get(i - 1).getPlace(), lowerAccuracyDestinations);
                        }
                    }
                }
            }
        }
    }

    /**
     * Checks if the user is still at the last visit location
     * @param startLocation user's current location
     * @param lastVisit the last known visit
     * @return true if the user is still at the location, false if not
     */
    private boolean userStillAtLastVisitLocation(StartLocation startLocation, Visit lastVisit) {
        return Math.abs(startLocation.getTimestamp() - lastVisit.getExitTime()) < GpsPointClusterizer.TIME_SPENT_IN_CLUSTER_THRESHOLD * 2
                && startLocation.distanceTo(lastVisit.getPlace().getCoordinate()) < GpsPointClusterizer.CLUSTER_RADIUS;
    }

    /**
     * Adds a possible next destination to hashMap and increments the number of visits to that place from
     * current location by one.
     *
     * @param nextDestination possible next destination
     * @param destinationMap hashmap where the place is added
     */
    private void addToNextDestinations(Place nextDestination, Map<Place, Integer> destinationMap) {
        int count = destinationMap.containsKey(nextDestination) ? destinationMap.get(nextDestination) : 0;
        destinationMap.put(nextDestination, count + 1);
    }
}

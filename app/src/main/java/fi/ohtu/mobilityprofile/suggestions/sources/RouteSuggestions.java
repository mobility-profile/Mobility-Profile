package fi.ohtu.mobilityprofile.suggestions.sources;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fi.ohtu.mobilityprofile.data.RouteSearchDao;
import fi.ohtu.mobilityprofile.domain.GpsPoint;
import fi.ohtu.mobilityprofile.domain.RouteSearch;
import fi.ohtu.mobilityprofile.domain.StartLocation;
import fi.ohtu.mobilityprofile.suggestions.Suggestion;
import fi.ohtu.mobilityprofile.suggestions.SuggestionAccuracy;
import fi.ohtu.mobilityprofile.suggestions.SuggestionSource;
import fi.ohtu.mobilityprofile.suggestions.locationHistory.GpsPointClusterizer;

/**
 * This class creates suggestions based on previous route searches user has made.
 */
public class RouteSuggestions implements SuggestionSource {

    /**
     * Creates the RouteSuggestions.
     */
    public RouteSuggestions() {
    }

    /**
     * Returns a list of probable locations the user would like to visit based on the previous
     * route searches the user has made. A route search is considered valid if it was max 2 hours
     * earlier or max 2 hours later that the current time.
     *
     * @param startLocation Starting location
     * @return List of probable destinations
     */
    @Override
    public List<Suggestion> getSuggestions(StartLocation startLocation) {
        List<Suggestion> suggestions = new ArrayList<>();
        Set<String> destinations = new HashSet<>();

        int counter = 0;

        for (RouteSearch route : RouteSearchDao.getAllRouteSearches()) {
            if (route.getStartCoordinates().distanceTo(startLocation.getCoordinate()) < GpsPointClusterizer.CLUSTER_RADIUS) {
                if (aroundTheSameTime(new Time(route.getTimestamp()), 2, 2)) {
                    if (destinations.contains(route.getDestination())) {
                        continue; // Don't add the same suggestion more than once.
                    }

                    Suggestion suggestion = new Suggestion(route.getDestination(), SuggestionAccuracy.HIGH, ROUTE_SUGGESTION, route.getDestinationCoordinates());
                    suggestions.add(suggestion);

                    destinations.add(route.getDestination());

                    counter++;
                    if (counter >= 3) break; // Only suggest 3 most recent searches at most.
                }
            }
        }


        return suggestions;
    }

    /**
     * Checks if the selected location was visited or set as destination around the same time in the past,
     * max x hours earlier or max y hours later than current time.
     *
     * @param visitTime    timestamp of the location
     * @param hoursEarlier hours earlier than current time
     * @param hoursLater   hours later than current time
     * @return true if location was visited within the time frame, false if not.
     */
    public static boolean aroundTheSameTime(Time visitTime, int hoursEarlier, int hoursLater) {
        Date currentTime = new Date(System.currentTimeMillis());

        int visitHour = visitTime.getHours();
        int visitMin = visitTime.getMinutes();
        int currentHour = currentTime.getHours();
        int currentMin = currentTime.getMinutes();

        return (visitHour > currentHour - hoursEarlier || (visitHour == currentHour - hoursEarlier && visitMin >= currentMin))
                && (visitHour < currentHour + hoursLater || (visitHour == currentHour + hoursLater && visitMin <= currentMin));
    }
}

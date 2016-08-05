package fi.ohtu.mobilityprofile.suggestions.sources;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fi.ohtu.mobilityprofile.Util;
import fi.ohtu.mobilityprofile.data.RouteSearchDao;
import fi.ohtu.mobilityprofile.domain.Place;
import fi.ohtu.mobilityprofile.domain.RouteSearch;
import fi.ohtu.mobilityprofile.suggestions.Suggestion;
import fi.ohtu.mobilityprofile.suggestions.SuggestionAccuracy;
import fi.ohtu.mobilityprofile.suggestions.SuggestionSource;

/**
 * This class creates suggestions based on previous route searches user has made.
 */
public class RouteSuggestions implements SuggestionSource {

    /**
     * Creates the RouteSuggestions.
     *
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
     *
     */
    @Override
    public List<Suggestion> getSuggestions(Place startLocation) {
        List<Suggestion> suggestions = new ArrayList<>();
        Set<String> destinations = new HashSet<>();

        int counter = 0;
        for (RouteSearch route : RouteSearchDao.getAllRouteSearches()) {
            if (Util.aroundTheSameTime(new Time(route.getTimestamp()), 2, 2)) {
                if (destinations.contains(route.getDestination())) continue; // Don't add the same suggestion more than once.

                Suggestion suggestion = new Suggestion(route.getDestination(), SuggestionAccuracy.HIGH, ROUTE_SUGGESTION);
                suggestions.add(suggestion);

                destinations.add(route.getDestination());

                counter++;
                if (counter >= 3) break; // Only suggest 3 most recent searches at most.
            }
        }

        return suggestions;
    }
}

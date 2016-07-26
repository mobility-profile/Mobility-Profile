package fi.ohtu.mobilityprofile.suggestions;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import fi.ohtu.mobilityprofile.Util;
import fi.ohtu.mobilityprofile.data.RouteSearchDao;
import fi.ohtu.mobilityprofile.domain.RouteSearch;

/**
 * This class creates suggestions based on previous route searches user has made.
 */
public class RouteSuggestions implements SuggestionSource {
    private RouteSearchDao routeSearchDao;

    /**
     * Creates the RouteSuggestions.
     *
     * @param routeSearchDao DAO for saved route searches
     */
    public RouteSuggestions(RouteSearchDao routeSearchDao) {
        this.routeSearchDao = routeSearchDao;
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
    public List<Suggestion> getSuggestions(String startLocation) {
        List<Suggestion> suggestions = new ArrayList<>();

        for (RouteSearch route : routeSearchDao.getRouteSearchesByStartlocation(startLocation)) {
            if (Util.aroundTheSameTime(new Time(route.getTimestamp()), 2, 2)) {
                Suggestion suggestion = new Suggestion(route.getDestination(), SuggestionAccuracy.HIGH, ROUTE_SUGGESTION);
                suggestions.add(suggestion);
            }
        }

        return suggestions;
    }
}

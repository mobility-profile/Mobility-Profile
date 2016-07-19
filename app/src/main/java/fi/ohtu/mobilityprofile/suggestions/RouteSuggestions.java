package fi.ohtu.mobilityprofile.suggestions;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fi.ohtu.mobilityprofile.Util;
import fi.ohtu.mobilityprofile.data.RouteSearchDao;
import fi.ohtu.mobilityprofile.domain.RouteSearch;

public class RouteSuggestions implements SuggestionSource {
    private RouteSearchDao routeSearchDao;

    public RouteSuggestions(RouteSearchDao routeSearchDao) {
        this.routeSearchDao = routeSearchDao;
    }

    /**
     * Selects destination based on previously used routes.
     * Checks if the user has gone to some destination at the same time in the past,
     * max 2 hours earlier or max 2 hours later than current time.
     * Searches from previously used routes.
     *
     * @param startLocation Starting location
     * @return Previously used destination
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

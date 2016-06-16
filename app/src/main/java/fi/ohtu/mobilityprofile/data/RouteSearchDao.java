package fi.ohtu.mobilityprofile.data;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

/**
 * DAO used for saving and reading RouteSearches to/from the database.
 */
public class RouteSearchDao {

    /**
     * Returns the latest routesearch from the database, or null if there is none.
     *
     * @return Latest routesearch
     */
    public RouteSearch getLatestRouteSearch() {
        return getLatestRouteSearch(Select.from(RouteSearch.class)
                .orderBy("timestamp DESC")
                .limit("1"));
    }

    private RouteSearch getLatestRouteSearch(Select<RouteSearch> query) {
        List<RouteSearch> routesearches = query.list();

        assert routesearches.size() <= 1 : "Invalid SQL query: only one or zero entities should have been returned!";

        if (routesearches.size() == 0) {
            return null;
        }

        return routesearches.get(0);
    }

    /**
     * Returns a list of routesearches where the nearestKnownLocation matches the given one.
     *
     * @param location Location of the routesearches
     * @return List of routesearches
     */
    public List<RouteSearch> getRouteSearchesByLocation(String location) {
        List<RouteSearch> searches = Select.from(RouteSearch.class)
                .where(Condition.prop("nearestKnownLocation").eq(location))
                .orderBy("timestamp DESC")
                .list();

        return searches;
    }

    /**
     * Saves a routesearch to the database.
     *
     * @param routeSearch routesearch to be saved
     */
    public void insertRouteSearch(RouteSearch routeSearch) {
        routeSearch.save();
    }
}

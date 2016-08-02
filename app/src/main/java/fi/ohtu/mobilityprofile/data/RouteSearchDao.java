package fi.ohtu.mobilityprofile.data;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

import fi.ohtu.mobilityprofile.domain.RouteSearch;

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

    /**
     * Returns the latest routesearch from the database based on custom query,
     * or null if there is none.
     * @param query custom query
     * @return result of the query
     */
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
     * @param startLocation StartLocation of the routesearches
     * @return List of routesearches
     */
    public List<RouteSearch> getRouteSearchesByStartlocation(String startLocation) {
        List<RouteSearch> searches = Select.from(RouteSearch.class)
                .where(Condition.prop("startlocation").eq(startLocation))
                .orderBy("timestamp DESC")
                .list();

        return searches;
    }

    /**
     * Returns a list of routesearches where the startlocation matches the given one.
     *
     * @param destination destination of the routesearches
     * @return List of routesearches
     */
    public List<RouteSearch> getRouteSearchesByDestination(String destination) {
        List<RouteSearch> searches = Select.from(RouteSearch.class)
                .where(Condition.prop("destination").eq(destination))
                .orderBy("timestamp DESC")
                .list();

        return searches;
    }

    /**
     * Returns a list of routesearches where the startlocation matches the given one.
     *
     * @param startLocation Start location of the routesearche
     * @param destination Destination of the routesearch
     * @return List of routesearches
     */
    public List<RouteSearch> getRouteSearchesByStartlocationAndDestination(String startLocation, String destination) {
        List<RouteSearch> searches = Select.from(RouteSearch.class)
                .where(Condition.prop("startlocation").eq(startLocation))
                .where(Condition.prop("destination").eq(destination))
                .orderBy("timestamp DESC")
                .list();

        return searches;
    }

    /**
     * Returns all saved routesearches.
     *
     * @return List of routesearches
     */
    public List<RouteSearch> getAllRouteSearches() {
        List<RouteSearch> searches = Select.from(RouteSearch.class)
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

    /**
     * Deletes all RouteSearch data from the database.
     */
    public static void deleteAllData() {
        RouteSearch.deleteAll(RouteSearch.class);
    }
}

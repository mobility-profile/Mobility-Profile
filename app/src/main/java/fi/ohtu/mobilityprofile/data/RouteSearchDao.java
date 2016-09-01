package fi.ohtu.mobilityprofile.data;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

import fi.ohtu.mobilityprofile.domain.RouteSearch;

/**
 * RouteSearchDAO is used for saving and reading RouteSearches to/from the database.
 */
public class RouteSearchDao {

    /**
     * Returns the latest routesearch from the database, or null if there is none.
     *
     * @return Latest routesearch
     */
    public static RouteSearch getLatestRouteSearch() {
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
    private static RouteSearch getLatestRouteSearch(Select<RouteSearch> query) {
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
    public static List<RouteSearch> getRouteSearchesByStartlocation(String startLocation) {
        return Select.from(RouteSearch.class)
                .where(Condition.prop("startlocation").eq(startLocation))
                .orderBy("timestamp DESC")
                .list();
    }

    /**
     * Returns a list of routesearches where the destination matches the given one.
     *
     * @param destination destination of the routesearches
     * @return List of routesearches
     */
    public static List<RouteSearch> getRouteSearchesByDestination(String destination) {
        return Select.from(RouteSearch.class)
                .where(Condition.prop("destination").eq(destination))
                .orderBy("timestamp DESC")
                .list();
    }

    /**
     * Returns a list of routesearches where the startlocation and destination matches the given ones.
     *
     * @param startLocation Start location of the routesearch
     * @param destination Destination of the routesearch
     * @return List of routesearches
     */
    public static List<RouteSearch> getRouteSearchesByStartlocationAndDestination(String startLocation, String destination) {
        return Select.from(RouteSearch.class)
                .where(Condition.prop("startlocation").eq(startLocation))
                .where(Condition.prop("destination").eq(destination))
                .orderBy("timestamp DESC")
                .list();
    }

    /**
     * Returns all saved routesearches.
     *
     * @return List of routesearches
     */
    public static List<RouteSearch> getAllRouteSearches() {
        return Select.from(RouteSearch.class)
                .orderBy("timestamp DESC")
                .list();
    }

    /**
     * Saves a routesearch to the database.
     *
     * @param routeSearch routesearch to be saved
     */
    public static void insertRouteSearch(RouteSearch routeSearch) {
        if (routeSearch.getStartCoordinates() != null) routeSearch.getStartCoordinates().save();
        if (routeSearch.getDestinationCoordinates() != null) routeSearch.getDestinationCoordinates().save();
        routeSearch.save();
    }

    /**
     * Retrieves all RouteSearches with the given mode
     * @param mode 0 for intracity, 1 for intercity
     * @return list of routeSearches
     */
    public static List<RouteSearch> getAll(int mode) {
        return Select.from(RouteSearch.class)
                .where(Condition.prop("mode").eq(mode))
                .orderBy("timestamp DESC")
                .list();
    }

    /**
     * Deletes all RouteSearch data from the database.
     */
    public static void deleteAllData() {
        RouteSearch.deleteAll(RouteSearch.class);
    }
}

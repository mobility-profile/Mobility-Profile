package fi.ohtu.mobilityprofile.data;

import com.orm.query.Select;

import java.util.List;

import fi.ohtu.mobilityprofile.domain.InterCitySearch;
import fi.ohtu.mobilityprofile.domain.RouteSearch;

public class InterCitySearchDao {

    /**
     * Returns all saved searches.
     *
     * @return List of searcher
     */
    public static List<InterCitySearch> getAllSearches() {
        List<InterCitySearch> searches = Select.from(InterCitySearch.class)
                .orderBy("timestamp DESC")
                .list();

        return searches;
    }

    /**
     * Saves a search to the database.
     *
     * @param search Search to be saved
     */
    public static void insertInterCitySearch(InterCitySearch search) {
        search.save();
    }

    /**
     * Deletes all search data from the database.
     */
    public static void deleteAllData() {
        InterCitySearch.deleteAll(InterCitySearch.class);
    }
}

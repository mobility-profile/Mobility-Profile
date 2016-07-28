package fi.ohtu.mobilityprofile.data;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

import fi.ohtu.mobilityprofile.domain.Place;

/**
 * DAO used for saving and reading Visits to/from the database.
 */
public class PlaceDao {
    private SignificantPlaceDao significantPlaceDao;

    /**
     * Creates PlaceDao.
     * @param significantPlaceDao
     */
    public PlaceDao(SignificantPlaceDao significantPlaceDao) {
        this.significantPlaceDao = significantPlaceDao;
    }

    /**
     * Returns the latest visit from the database, or null if there is none.
     *
     * @return Latest visit
     */
    public Place getLatestVisit() {
        return getLatestVisit(Select.from(Place.class)
                .orderBy("timestamp DESC")
                .limit("1"));
    }

    /**
     * Returns the latest visit from the database based on custom query,
     * or null if there is none.
     * @param query custom query
     * @return latest visit
     */
    private Place getLatestVisit(Select<Place> query) {
        List<Place> places = query.list();

        assert places.size() <= 1 : "Invalid SQL query: only one or zero entities should have been returned!";

        if (places.size() == 0) {
            return null;
        }

        return places.get(0);
    }

    /**
     * Returns a list of visits where the nearestKnownLocation matches the given one.
     *
     * @param location SignificantPlace of the visits
     * @return List of visits
     */
    public List<Place> getVisitsByLocation(String location) {
        List<Place> places = Select.from(Place.class)
                .where(Condition.prop("original_location").eq(location))
                .orderBy("timestamp DESC")
                .list();

        return places;
    }

    /**
     * Returns a list of all visits.
     * @return list of visits
     */
    public static List<Place> getAll() {
        return Select.from(Place.class)
                .orderBy("timestamp DESC")
                .list();
    }

    /**
     * Saves a place to the database.
     *
     * @param place Place to be saved
     */
    public void insertVisit(Place place) {
        place.save();
    }

    /**
     * Deletes all Place data from the database
     */
    public static void deleteAllData() {
        SignificantPlaceDao.deleteAllData();
        Place.deleteAll(Place.class);
    }
}
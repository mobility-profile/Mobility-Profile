package fi.ohtu.mobilityprofile.data;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

import fi.ohtu.mobilityprofile.domain.Place;

/**
 * DAO used for saving and reading Visits to/from the database.
 */
public class PlaceDao {

    /**
     * Returns the latest place from the database, or null if there is none.
     *
     * @return Latest visit
     */
    public static Place getLatest() {
        List<Place> places = Select.from(Place.class).orderBy("timestamp DESC").limit("1").list();
        assert places.size() <= 1 : "Invalid SQL query: only one or zero entities should have been returned!";
        if (places.size() == 0) {
            return null;
        }
        return places.get(0);
    }

    /**
     * Returns a list of all visits.
     *
     * @return list of visits
     */
    public static List<Place> getAll() {
        return Select.from(Place.class).orderBy("timestamp DESC").list();
    }

    /**
     * Saves a place to the database.
     *
     * @param place Place to be saved
     */
    public static void insert(Place place) {
        place.save();
    }

    /**
     * Deletes all Place data from the database
     */
    public static void deleteAllData() {
        Place.deleteAll(Place.class);
    }
}
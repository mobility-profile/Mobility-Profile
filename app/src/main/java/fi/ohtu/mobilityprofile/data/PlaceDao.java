package fi.ohtu.mobilityprofile.data;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

import fi.ohtu.mobilityprofile.domain.Coordinate;
import fi.ohtu.mobilityprofile.domain.Place;

/**
 * DAO used for clustering visited locations.
 */
public class PlaceDao {

    /**
     * Returns a Place closest to given coordinates
     * @param coordinate coordinates of the given location
     * @return Place
     */
    public static Place getPlaceClosestTo(Coordinate coordinate) {
        List<Place> places = getAll();
        Place result = null;
        double distance = Double.MAX_VALUE;
        for(Place place : places) {
            if(place.distanceTo(coordinate) < distance){
                distance = place.distanceTo(coordinate);
                result = place;
            }
        }
        return result;
    }

    public static List<Place> getAll() {
        return Place.listAll(Place.class);
    }


    /**
     * Saves a Place to the database.
     * @param place Place to be saved
     */
    public static void insertPlace(Place place) {
        place.getCoordinate().save();
        place.save();
    }

    /**
     * Finds a Place by name
     * @param name name of the Place
     * @return Place with the given name
     */

    public static Place getPlaceByName(String name) {
        List<Place> places = Select.from(Place.class)
                .where(Condition.prop("name").eq(name))
                .limit("1")
                .list();

        assert places.size() <= 1 : "Invalid SQL query: only one or zero entities should have been returned!";

        return  (places.size() == 1) ? places.get(0) : null;
    }

    /**
     * Finds a Place by name
     * @param address address of the Place
     * @return Place with the given address
     */
    public static Place getPlaceByAddress(String address) {
        List<Place> places = Select.from(Place.class)
                .where(Condition.prop("address").eq(address))
                .limit("1")
                .list();

        assert places.size() <= 1 : "Invalid SQL query: only one or zero entities should have been returned!";

        return  (places.size() == 1) ? places.get(0) : null;
    }

    /**
     * Deletes a Place by address
     * @param address address of the Place to be deleted
     */
    public static void deletePlaceByAddress(String address) {
        Place place = getPlaceByAddress(address);
        if (place != null) {
            place.delete();
        }
    }

    /**
     * Deletes all Place data from the database
     */
    public static void deleteAllData() {
        Place.deleteAll(Place.class);
    }
}

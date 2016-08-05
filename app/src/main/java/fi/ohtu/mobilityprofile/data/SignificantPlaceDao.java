package fi.ohtu.mobilityprofile.data;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

import fi.ohtu.mobilityprofile.domain.Coordinate;
import fi.ohtu.mobilityprofile.domain.SignificantPlace;

/**
 * DAO used for clustering visited locations.
 */
public class SignificantPlaceDao {

    /**
     * Returns a SignificantPlace closest to given coordinates
     * @param coordinate coordinates of the given location
     * @return SignificantPlace
     */
    public static SignificantPlace getSignificantPlaceClosestTo(Coordinate coordinate) {
        List<SignificantPlace> significantPlaces = getAll();
        SignificantPlace result = null;
        double distance = Double.MAX_VALUE;
        for(SignificantPlace sPlace : significantPlaces) {
            if(sPlace.getCoordinate().distanceTo(coordinate) < distance){
                result = sPlace;
            }
        }
        return result;
    }

    public static List<SignificantPlace> getAll() {
        return SignificantPlace.listAll(SignificantPlace.class);
    }


    /**
     * Saves a SignificantPlace to the database.
     * @param significantPlace SignificantPlace to be saved
     */
    public static void insertSignificantPlace(SignificantPlace significantPlace) {
        significantPlace.save();
    }

    /**
     * Finds a SignificantPlace by name
     * @param name name of the significantPlace
     * @return SignificantPlace with the given name
     */

    public static SignificantPlace getSignificantPlaceByName(String name) {
        List<SignificantPlace> places = Select.from(SignificantPlace.class)
                .where(Condition.prop("name").eq(name))
                .limit("1")
                .list();

        assert places.size() <= 1 : "Invalid SQL query: only one or zero entities should have been returned!";

        return  (places.size() == 1) ? places.get(0) : null;
    }

    /**
     * Finds a significantPlace by name
     * @param address address of the significantPlace
     * @return SignificantPlace with the given address
     */
    public static SignificantPlace getSignificantPlaceByAddress(String address) {
        List<SignificantPlace> places = Select.from(SignificantPlace.class)
                .where(Condition.prop("address").eq(address))
                .limit("1")
                .list();

        assert places.size() <= 1 : "Invalid SQL query: only one or zero entities should have been returned!";

        return  (places.size() == 1) ? places.get(0) : null;
    }

    /**
     * Deletes a SignificantPlace by address
     * @param address address of the significantPlace to be deleted
     */
    public static void deleteSignificantPlaceByAddress(String address) {
        SignificantPlace place = getSignificantPlaceByAddress(address);
        if (place != null) {
            place.delete();
        }
    }

    /**
     * Deletes all SignificantPlace data from the database
     */
    public static void deleteAllData() {
        SignificantPlace.deleteAll(SignificantPlace.class);
    }
}

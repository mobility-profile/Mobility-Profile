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

    public SignificantPlace getSignificantPlaceClosestTo(Coordinate coordinate) {
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

    public List<SignificantPlace> getAll() {
        return SignificantPlace.listAll(SignificantPlace.class);
    }

    /**
     * Returns the nearest known SignificantPlace from the searchLocation if it is within searchRadius.
     * If no SignificantPlaces were found, new one will be created with the given searchLocation. The
     * new SignificantPlace will then be saved to the database and returned.
     *
     * @param searchLocation Search nearestKnownLocation
     * @param searchRadius Search radius in meters
     * @return Nearest SignificantPlace
     */
    public SignificantPlace getNearestLocation(String searchLocation, int searchRadius) {
        List<SignificantPlace> significantPlaces = SignificantPlace.listAll(SignificantPlace.class);

        SignificantPlace nearestSignificantPlace = null;
        for (SignificantPlace significantPlace : significantPlaces) {
            if (significantPlace.getAddress().equals(searchLocation)) {
                nearestSignificantPlace = significantPlace;
                break;
                // TODO: Check if significantPlace is within searchRadius from the searchLocation.
                // Also check all significantPlaces to make sure we find the closest one.
            }
        }

        if (nearestSignificantPlace == null) {
            // There weren't any saved significantPlaces within searchRadius, so just create a new one and
            // save it to the database.
            nearestSignificantPlace = new SignificantPlace();
            nearestSignificantPlace.save();
        }

        return nearestSignificantPlace;
    }

    /**
     * Saves a SignificantPlace to the database.
     * @param significantPlace
     */
    public void insertSignificantPlace(SignificantPlace significantPlace) {
        //significantPlace.getCoordinate().save();
        significantPlace.save();
    }

    /**
     * Finds a SignificantPlace based on name
     */

    public SignificantPlace getSignificantPlaceBasedOnName(String name) {
        List<SignificantPlace> places = Select.from(SignificantPlace.class)
                .where(Condition.prop("name").eq(name))
                .limit("1")
                .list();

        assert places.size() <= 1 : "Invalid SQL query: only one or zero entities should have been returned!";

        return  (places.size() == 1) ? places.get(0) : null;
    }

    /**
     * Deletes a SignificantPlace based on name
     *
     */
    public void deleteSignificantPlaceBasedOnName(String name) {
        SignificantPlace place = getSignificantPlaceBasedOnName(name);
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

package fi.ohtu.mobilityprofile.data;

import com.orm.query.Select;

import java.util.List;

import fi.ohtu.mobilityprofile.domain.Place;
import fi.ohtu.mobilityprofile.domain.SignificantPlace;

/**
 * DAO used for clustering visited locations.
 */
public class SignificantPlaceDao {

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
            if (significantPlace.getLocation().equals(searchLocation)) {
                nearestSignificantPlace = significantPlace;
                break;
                // TODO: Check if significantPlace is within searchRadius from the searchLocation.
                // Also check all significantPlaces to make sure we find the closest one.
            }
        }

        if (nearestSignificantPlace == null) {
            // There weren't any saved significantPlaces within searchRadius, so just create a new one and
            // save it to the database.
            nearestSignificantPlace = new SignificantPlace(searchLocation, System.currentTimeMillis());
            nearestSignificantPlace.save();
        }

        return nearestSignificantPlace;
    }

    /**
     * Checks if the current location is within SignificantPlace and if it is, returns SignificantPlace.
     * If location is not a SignificantPlace, null is returned.
     * @param searchLocation Nearest known location
     * @param searchRadius Search radius in meters
     * @return SignificantPlace if current location is one, null otherwise
     */
    public SignificantPlace getSignificantPlaceIfCurrentLocationIsOne(String searchLocation, int searchRadius) {

        return null;
    }

    /**
     * Returns a list of all SignificantPlaces.
     * @return List of SignificantPlaces.
     */
    public List<SignificantPlace> getAllSignificantPlaces() {
        return Select.from(SignificantPlace.class)
                .orderBy("timestamp DESC")
                .list();
    }

    /**
     * Saves a SignificantPlace to the database.
     * @param significantPlace SignificantPlace to be saved
     */
    public void insertSignificantPlace(SignificantPlace significantPlace) {
        significantPlace.save();
    }
    /**
     * Deletes all SignificantPlace data from the database
     */
    public static void deleteAllData() {
        SignificantPlace.deleteAll(SignificantPlace.class);
    }
}

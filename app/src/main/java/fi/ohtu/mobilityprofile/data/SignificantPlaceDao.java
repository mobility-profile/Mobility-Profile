package fi.ohtu.mobilityprofile.data;

import java.util.List;

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
            nearestSignificantPlace = new SignificantPlace();
            nearestSignificantPlace.save();
        }

        return nearestSignificantPlace;
    }

    /**
     * Saves a Visit to the database.
     * @param significantPlace
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

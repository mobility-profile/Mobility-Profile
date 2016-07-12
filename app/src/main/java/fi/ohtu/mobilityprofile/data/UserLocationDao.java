package fi.ohtu.mobilityprofile.data;

import java.util.List;

import fi.ohtu.mobilityprofile.domain.UserLocation;

/**
 * DAO used for clustering visited locations.
 */
public class UserLocationDao {
    /**
     * Returns the nearest known UserLocation from the searchLocation if it is within searchRadius.
     * If no UserLocations were found, new one will be created with the given searchLocation. The
     * new UserLocation will then be saved to the database and returned.
     *
     * @param searchLocation Search nearestKnownLocation
     * @param searchRadius Search radius in meters
     * @return Nearest UserLocation
     */
    public UserLocation getNearestLocation(String searchLocation, int searchRadius) {
        List<UserLocation> userLocations = UserLocation.listAll(UserLocation.class);

        UserLocation nearestLocation = null;
        for (UserLocation userLocation : userLocations) {
            if (userLocation.getLocation().equals(searchLocation)) {
                nearestLocation = userLocation;
                break;
                // TODO: Check if userLocation is within searchRadius from the searchLocation.
                // Also check all userLocations to make sure we find the closest one.
            }
        }

        if (nearestLocation == null) {
            // There weren't any saved locations within searchRadius, so just create a new one and
            // save it to the database.
            nearestLocation = new UserLocation(searchLocation);
            nearestLocation.save();
        }

        return nearestLocation;
    }

    /**
     * Deletes all UserLocation data from the database
     */
    public static void deleteAllData() {
        UserLocation.deleteAll(UserLocation.class);
    }
}

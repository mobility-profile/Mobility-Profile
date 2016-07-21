package fi.ohtu.mobilityprofile.data;

import java.util.List;

import fi.ohtu.mobilityprofile.domain.Location;

/**
 * DAO used for clustering visited locations.
 */
public class UserLocationDao {

    /**
     * Returns the nearest known Location from the searchLocation if it is within searchRadius.
     * If no UserLocations were found, new one will be created with the given searchLocation. The
     * new Location will then be saved to the database and returned.
     *
     * @param searchLocation Search nearestKnownLocation
     * @param searchRadius Search radius in meters
     * @return Nearest Location
     */
    public Location getNearestLocation(String searchLocation, int searchRadius) {
        List<Location> locations = Location.listAll(Location.class);

        Location nearestLocation = null;
        for (Location location : locations) {
            if (location.getLocation().equals(searchLocation)) {
                nearestLocation = location;
                break;
                // TODO: Check if location is within searchRadius from the searchLocation.
                // Also check all locations to make sure we find the closest one.
            }
        }

        if (nearestLocation == null) {
            // There weren't any saved locations within searchRadius, so just create a new one and
            // save it to the database.
            nearestLocation = new Location(searchLocation);
            nearestLocation.save();
        }

        return nearestLocation;
    }

    /**
     * Deletes all Location data from the database
     */
    public static void deleteAllData() {
        Location.deleteAll(Location.class);
    }
}

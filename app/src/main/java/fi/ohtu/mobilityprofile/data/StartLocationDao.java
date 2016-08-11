package fi.ohtu.mobilityprofile.data;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

import fi.ohtu.mobilityprofile.domain.GpsPoint;
import fi.ohtu.mobilityprofile.domain.StartLocation;

/**
 * DAO used for saving and reading GPSPoints to/from the database.
 */
public class StartLocationDao {

    /**
     * Returns the latest GpsPoint from the database, or null if there is none.
     *
     * @return Latest GpsPoint
     */
    public static StartLocation getStartLocation() {
        List<StartLocation> startLocations = Select.from(StartLocation.class).orderBy("timestamp DESC").limit("1").list();
        assert startLocations.size() <= 1 : "Invalid SQL query: only one or zero entities should have been returned!";
        if (startLocations.size() == 0) {
            return null;
        }
        return startLocations.get(0);
    }

    /**
     * Returns a list of all GpsPoints.
     *
     * @return list of GpsPoints
     */
    private static List<StartLocation> getAll() {
        return Select.from(StartLocation.class).orderBy("timestamp ASC").list();
    }

    /**
     * Saves a GpsPoint to the database.
     *
     * @param startLocation GpsPoint to be saved
     */
    public static void insert(StartLocation startLocation) {
        deleteAllData();
        startLocation.getCoordinate().save();
        startLocation.save();
    }

    /**
     * Deletes all GpsPoint data from the database
     */
    private static void deleteAllData() {
        for(StartLocation startLocation : getAll()) {
            startLocation.getCoordinate().delete();
            startLocation.delete();
        }
    }

}

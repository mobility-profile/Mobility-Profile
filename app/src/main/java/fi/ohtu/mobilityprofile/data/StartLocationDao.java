package fi.ohtu.mobilityprofile.data;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

import fi.ohtu.mobilityprofile.domain.GpsPoint;
import fi.ohtu.mobilityprofile.domain.StartLocation;

/**
 * StartLocationDAO used for saving and reading StartLocations to/from the database.
 */
public class StartLocationDao {

    /**
     * Returns the latest StartLocation from the database, or null if there is none.
     *
     * @return Latest StartLocation
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
     * Returns a list of all StartLocations.
     *
     * @return list of StartLocations
     */
    private static List<StartLocation> getAll() {
        return Select.from(StartLocation.class).orderBy("timestamp ASC").list();
    }

    /**
     * Saves a StartLocation to the database.
     *
     * @param startLocation StartLocation to be saved
     */
    public static void insert(StartLocation startLocation) {
        deleteAllData();
        startLocation.getCoordinate().save();
        startLocation.save();
    }

    /**
     * Deletes all StartLocation data from the database
     */
    private static void deleteAllData() {
        for(StartLocation startLocation : getAll()) {
            if (startLocation.getCoordinate() != null) {
                startLocation.getCoordinate().delete();
            }
        }
        StartLocation.deleteAll(StartLocation.class);
    }

}

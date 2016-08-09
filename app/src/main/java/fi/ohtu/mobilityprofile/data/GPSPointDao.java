package fi.ohtu.mobilityprofile.data;

import com.orm.query.Select;

import java.util.List;

import fi.ohtu.mobilityprofile.domain.GPSPoint;

/**
 * DAO used for saving and reading GPSPoints to/from the database.
 */
public class GPSPointDao {

    /**
     * Returns the latest GPSPoint from the database, or null if there is none.
     *
     * @return Latest GPSPoint
     */
    public static GPSPoint getLatest() {
        List<GPSPoint> GPSPoints = Select.from(GPSPoint.class).orderBy("timestamp DESC").limit("1").list();
        assert GPSPoints.size() <= 1 : "Invalid SQL query: only one or zero entities should have been returned!";
        if (GPSPoints.size() == 0) {
            return null;
        }
        return GPSPoints.get(0);
    }

    /**
     * Returns a list of all GPSPoints.
     *
     * @return list of GPSPoints
     */
    public static List<GPSPoint> getAll() {
        return Select.from(GPSPoint.class).orderBy("timestamp ASC").list();
    }

    /**
     * Saves a GPSPoint to the database.
     *
     * @param gpsPoint GPSPoint to be saved
     */
    public static void insert(GPSPoint gpsPoint) {
        gpsPoint.getCoordinate().save();
        gpsPoint.save();
    }

    public static void delete(GPSPoint gpsPoint) {
        gpsPoint.delete();
    }

    /**
     * Deletes all GPSPoint data from the database
     */
    public static void deleteAllData() {
        GPSPoint.deleteAll(GPSPoint.class);
    }
}
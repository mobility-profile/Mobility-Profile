package fi.ohtu.mobilityprofile.data;

import com.orm.query.Select;

import java.util.List;

import fi.ohtu.mobilityprofile.domain.GpsPoint;

/**
 * DAO used for saving and reading GPSPoints to/from the database.
 */
public class GpsPointDao {

    /**
     * Returns the latest GpsPoint from the database, or null if there is none.
     *
     * @return Latest GpsPoint
     */
    public static GpsPoint getLatest() {
        List<GpsPoint> gpsPoints = Select.from(GpsPoint.class).orderBy("timestamp DESC").limit("1").list();
        assert gpsPoints.size() <= 1 : "Invalid SQL query: only one or zero entities should have been returned!";
        if (gpsPoints.size() == 0) {
            return null;
        }
        return gpsPoints.get(0);
    }

    /**
     * Returns a list of all GPSPoints.
     *
     * @return list of GPSPoints
     */
    public static List<GpsPoint> getAll() {
        return Select.from(GpsPoint.class).orderBy("timestamp ASC").list();
    }

    /**
     * Saves a GpsPoint to the database.
     *
     * @param gpsPoint GpsPoint to be saved
     */
    public static void insert(GpsPoint gpsPoint) {
        gpsPoint.getCoordinate().save();
        gpsPoint.save();
    }

    public static void delete(GpsPoint gpsPoint) {
        gpsPoint.delete();
    }

    /**
     * Deletes all GpsPoint data from the database
     */
    public static void deleteAllData() {
        for(GpsPoint gpsPoint : getAll()) {
            gpsPoint.getCoordinate().delete();
            gpsPoint.delete();
        }
    }
}

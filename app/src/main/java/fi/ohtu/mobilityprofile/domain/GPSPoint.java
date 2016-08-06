package fi.ohtu.mobilityprofile.domain;

import com.orm.SugarRecord;

/**
 * Class is used to save raw gps data.
 */
public class GPSPoint extends SugarRecord implements HasCoordinate, Comparable<GPSPoint> {
    long timestamp;
    Coordinate coordinate;

    /**
     *
     */
    public GPSPoint() {
    }

    /**
     * Creates GPSPoint.
     * @param timestamp timestamp of the visit
     * @param latitude latitude
     * @param longitude longitude
     */
    public GPSPoint(long timestamp, Float latitude, Float longitude) {
        this.timestamp = timestamp;
        this.coordinate = new Coordinate(latitude, longitude);
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public Coordinate getCoordinate() {
        return this.coordinate;
    }

    public Float getLatitude() {
        return this.coordinate.getLatitude();
    }

    public Float getLongitude() {
        return this.coordinate.getLongitude();
    }

    @Override
    public String toString() {
        return "lat=" + " lon=";
    }

    @Override
    public double distanceTo(HasCoordinate hasCoordinate) {
        return this.coordinate.distanceTo(hasCoordinate.getCoordinate());
    }

    @Override
    public int compareTo(GPSPoint another) {
        long difference = timestamp - another.getTimestamp();
        if (difference < 0) {
            return -1;
        }
        if (difference > 0) {
            return 1;
        }
        return 0;
    }
}

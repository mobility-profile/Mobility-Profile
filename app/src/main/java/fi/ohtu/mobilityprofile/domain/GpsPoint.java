package fi.ohtu.mobilityprofile.domain;

import com.orm.SugarRecord;

/**
 * Class is used to save raw gps data.
 */
public class GpsPoint extends SugarRecord implements HasCoordinate, Comparable<GpsPoint> {
    long timestamp;
    Coordinate coordinate;

    /**
     *
     */
    public GpsPoint() {
        this.timestamp = 0;
        this.coordinate = new Coordinate(0f, 0f);
    }

    /**
     * Creates GpsPoint.
     * @param timestamp timestamp of the visit
     * @param latitude latitude
     * @param longitude longitude
     */
    public GpsPoint(long timestamp, Float latitude, Float longitude) {
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
    public int compareTo(GpsPoint another) {
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

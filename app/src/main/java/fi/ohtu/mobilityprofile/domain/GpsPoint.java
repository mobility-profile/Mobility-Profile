package fi.ohtu.mobilityprofile.domain;

import android.support.annotation.NonNull;

import com.orm.SugarRecord;

/**
 * Class is used to save raw gps data.
 */
public class GpsPoint extends SugarRecord implements Comparable<GpsPoint> {
    long timestamp;
    float accuracy;
    Coordinate coordinate;

    /**
     *
     */
    public GpsPoint() {
        this.timestamp = 0;
        this.accuracy = 0;
        this.coordinate = new Coordinate(0f, 0f);
        this.coordinate.save();
    }

    /**
     * Creates GpsPoint.
     * @param timestamp timestamp of the visit
     * @param accuracy accuracy of the gpsPoint location
     * @param latitude latitude of the location
     * @param longitude longitude of the location
     */
    public GpsPoint(long timestamp, float accuracy, float latitude, float longitude) {
        this.timestamp = timestamp;
        this.accuracy = accuracy;
        this.coordinate = new Coordinate(latitude, longitude);
        this.coordinate.save();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public float getAccuracy() {
        return accuracy;
    }

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

    /**
     * Returns the distance between this GpsPoint and given coordinate
     * @param coordinate the coordinate to be compared
     * @return distance
     */
    public double distanceTo(Coordinate coordinate) {
        return this.coordinate.distanceTo(coordinate);
    }

    @Override
    public int compareTo(@NonNull GpsPoint another) {
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

package fi.ohtu.mobilityprofile.domain;

import com.orm.SugarRecord;

/**
 * Class is used to save raw gps data.
 */
public class Place extends SugarRecord implements HasCoordinate {
    long timestamp;
    Coordinate coordinate;

    /**
     *
     */
    public Place() {
    }

    /**
     * Creates Place.
     * @param timestamp timestamp of the visit
     * @param latitude latitude
     * @param longitude longitude
     */
    public Place(long timestamp, Float latitude, Float longitude) {
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
    public long save() {
        this.coordinate.save();
        return super.save();
    }

    @Override
    public double distanceTo(HasCoordinate hasCoordinate) {
        return this.coordinate.distanceTo(hasCoordinate.getCoordinate());
    }

}

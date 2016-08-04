package fi.ohtu.mobilityprofile.domain;

import com.orm.SugarRecord;

/**
 * Class is used to save raw gps data.
 */
public class Place extends SugarRecord {
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

    /**
     * Returns the distance between this place and a given place
     * @param place Place to be compared
     * @return distance
     */
    public double distanceTo(Place place) {
        return this.coordinate.distanceTo(place.getCoordinate());
    }

    /**
     * Returns the distance between this place and a given coordinate
     * @param coordinate coordinate to be compared
     * @return distance
     */
    public double distanceTo(Coordinate coordinate) {
        return this.coordinate.distanceTo(coordinate);
    }
}

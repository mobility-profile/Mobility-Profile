package fi.ohtu.mobilityprofile.domain;

import com.orm.SugarRecord;

/**
 * Class is used to save raw gps data.
 */
public class Place extends SugarRecord {
    long timestamp;
    Float latitude;
    Float longitude;

    /**
     *
     */
    public Place() {
    }

    /**
     * Creates Place.
     * @param timestamp timestamp of the visit
     * @param latitude latitude of the location
     * @param longitude longitude of the location
     */
    public Place(long timestamp, Float latitude, Float longitude) {
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Float getLongitude() {
        return longitude;
    }

    public Float getLatitude() {
        return latitude;
    }

    @Override
    public String toString() {
        return "lat="+latitude+" lon="+longitude;
    }
}

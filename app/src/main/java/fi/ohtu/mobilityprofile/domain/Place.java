package fi.ohtu.mobilityprofile.domain;

import com.orm.SugarRecord;

/**
 * Class is used to save locations the user has visited.
 */
public class Place extends SugarRecord {

    long timestamp;
    String originalLocation;             // Accurate point the user visited.
    SignificantPlace nearestKnownLocation;   // Closest known nearestKnownLocation that is within 50 meters (value may change) from the actual nearestKnownLocation.
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
     * @param originalLocation exact location of the visit
     */
    public Place(long timestamp, String originalLocation) {
        this.timestamp = timestamp;
        this.originalLocation = originalLocation;
        this.nearestKnownLocation = null;
    }

    /**
     * Creates Place.
     * @param timestamp timestamp of the visit
     * @param originalLocation exact location of the visit
     * @param latitude latitude of the location
     * @param longitude longitude of the location
     */
    public Place(long timestamp, String originalLocation, Float latitude, Float longitude) {
        this.timestamp = timestamp;
        this.originalLocation = originalLocation;
        this.nearestKnownLocation = null;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getOriginalLocation() {
        return originalLocation;
    }

    public SignificantPlace getNearestKnownLocation() {
        return nearestKnownLocation;
    }

    public void setNearestKnownLocation(SignificantPlace nearestKnownLocation) {
        this.nearestKnownLocation = nearestKnownLocation;
    }

    public Float getLongitude() {
        return longitude;
    }

    public Float getLatitude() {
        return latitude;
    }
}

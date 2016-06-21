package fi.ohtu.mobilityprofile.data;

import com.orm.SugarRecord;

/**
 * Class is used to save locations the user has visited.
 */
public class Visit extends SugarRecord {

    long timestamp;
    String originalLocation;             // Accurate point the user visited.
    UserLocation nearestKnownLocation;   // Closest known nearestKnownLocation that is within 50 meters (value may change) from the actual nearestKnownLocation.

    public Visit() {
    }

    /**
     * Creates Visit.
     * @param timestamp timestamp of the visit
     * @param originalLocation exact location of the visit
     */
    public Visit(long timestamp, String originalLocation) {
        this.timestamp = timestamp;
        this.originalLocation = originalLocation;
        this.nearestKnownLocation = null;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getOriginalLocation() {
        return originalLocation;
    }

    public UserLocation getNearestKnownLocation() {
        return nearestKnownLocation;
    }
}

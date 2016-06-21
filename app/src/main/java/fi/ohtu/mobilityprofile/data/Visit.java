package fi.ohtu.mobilityprofile.data;

import android.graphics.PointF;

import com.orm.SugarRecord;

public class Visit extends SugarRecord {

    long timestamp;
    String originalLocation;             // Accurate point the user visited.
    UserLocation nearestknownlocation;   // Closest known nearestKnownLocation that is within 50 meters (value may change) from the actual nearestKnownLocation.

    /**
     *
     */
    public Visit() {
    }

    /**
     * @param timestamp
     * @param originalLocation
     */
    public Visit(long timestamp, String originalLocation) {
        this.timestamp = timestamp;
        this.originalLocation = originalLocation;
        this.nearestknownlocation = null;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getOriginalLocation() {
        return originalLocation;
    }

    public UserLocation getNearestKnownLocation() {
        return nearestknownlocation;
    }
}

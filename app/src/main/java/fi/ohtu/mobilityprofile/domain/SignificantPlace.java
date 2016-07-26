package fi.ohtu.mobilityprofile.domain;

import android.graphics.PointF;

import com.orm.SugarRecord;

/**
 * Class is used to save locations the user visits frequently.
 */
public class SignificantPlace extends SugarRecord {
    private String location;
    private long timestamp;
    private

    /**
     *
     */
    public SignificantPlace() {
    }

    /**
     * Creates new SignificantPlace
     * @param location user's location
     */
    public SignificantPlace(String location, long timestamp) {
        this.location = location;
        this.timestamp = timestamp;
    }

    public String getLocation() {
        return location;
    }

    public long getTimestamp() {
        return timestamp;
    }
}

package fi.ohtu.mobilityprofile.domain;

import android.graphics.PointF;

import com.orm.SugarRecord;

/**
 *
 */
public class UserLocation extends SugarRecord {
    String location;

    /**
     *
     */
    public UserLocation() {
    }

    /**
     * Creates new User location
     * @param location user's location
     */
    public UserLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }
}

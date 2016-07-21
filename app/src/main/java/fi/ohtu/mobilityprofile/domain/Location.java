package fi.ohtu.mobilityprofile.domain;

import android.graphics.PointF;

import com.orm.SugarRecord;

/**
 * Class is used to save locations the user has visited.
 */
public class Location extends SugarRecord {
    String location;

    /**
     *
     */
    public Location() {
    }

    /**
     * Creates new User location
     * @param location user's location
     */
    public Location(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }
}

package fi.ohtu.mobilityprofile.domain;

import android.graphics.PointF;

import com.orm.SugarRecord;

/**
 * Class is used to save locations the user has visited.
 */
public class SignificantPlace extends SugarRecord {
    String location;

    /**
     *
     */
    public SignificantPlace() {
    }

    /**
     * Creates new User location
     * @param location user's location
     */
    public SignificantPlace(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }
}

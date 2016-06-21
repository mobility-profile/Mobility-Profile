package fi.ohtu.mobilityprofile.data;

import android.graphics.PointF;

import com.orm.SugarRecord;

public class UserLocation extends SugarRecord {
    String location;

    public UserLocation() {
    }

    public UserLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }
}

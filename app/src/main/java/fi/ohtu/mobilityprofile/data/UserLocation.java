package fi.ohtu.mobilityprofile.data;

import android.graphics.PointF;

import com.orm.SugarRecord;

public class UserLocation extends SugarRecord {
    PointF location;

    public UserLocation() {
    }

    public UserLocation(PointF location) {
        this.location = location;
    }

    public PointF getLocation() {
        return location;
    }
}

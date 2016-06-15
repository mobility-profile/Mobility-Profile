package fi.ohtu.mobilityprofile.data;

import com.orm.SugarRecord;

public class Visit extends SugarRecord {
    public static final int GPS_TRACKED = 1;
    public static final int USER_SEARCH = 2;

    long timestamp;
    String location;
    int type;

    public Visit() {
    }

    public Visit(long timestamp, String location, int type) {
        this.timestamp = timestamp;
        this.location = location;
        this.type = type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getLocation() {
        return location;
    }

    public int getType() {
        return type;
    }
}

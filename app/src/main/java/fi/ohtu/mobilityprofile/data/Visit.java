package fi.ohtu.mobilityprofile.data;

import com.orm.SugarRecord;

public class Visit extends SugarRecord {
    long timestamp;
    String location;

    public Visit() {
    }

    public Visit(long timestamp, String location) {
        this.timestamp = timestamp;
        this.location = location;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getLocation() {
        return location;
    }
}

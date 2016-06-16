package fi.ohtu.mobilityprofile.data;

import com.orm.SugarRecord;

public class RouteSearch extends SugarRecord {

    long timestamp;
    String location;
    int counter;

    public RouteSearch() {

    }

    public RouteSearch(long timestamp, String location) {
        this.timestamp = timestamp;
        this.location = location;
        this.counter = 1;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getLocation() {
        return location;
    }

    public int getCounter() {
        return counter;
    }
}

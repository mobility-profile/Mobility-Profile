package fi.ohtu.mobilityprofile.domain;

import com.orm.SugarRecord;

public class InterCitySearch extends SugarRecord {
    String startlocation;
    String destination;
    long timestamp;

    public InterCitySearch() {
    }

    public InterCitySearch(String startlocation, String destination, long timestamp) {
        this.startlocation = startlocation;
        this.destination = destination;
        this.timestamp = timestamp;
    }

    public String getStartlocation() {
        return startlocation;
    }

    public String getDestination() {
        return destination;
    }

    public long getTimestamp() {
        return timestamp;
    }
}

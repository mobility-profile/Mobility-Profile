package fi.ohtu.mobilityprofile.data;

import com.orm.SugarRecord;

/**
 *
 */
public class RouteSearch extends SugarRecord {

    long timestamp;
    String startlocation;
    String destination;

    /**
     *
     */
    public RouteSearch() {

    }

    /**
     *
     * @param timestamp
     * @param startlocation
     * @param destination
     */
    public RouteSearch(long timestamp, String startlocation, String destination) {
        this.timestamp = timestamp;
        this.startlocation = startlocation;
        this.destination = destination;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getStartlocation() {
        return startlocation;
    }

    public String getDestination() {
        return destination;
    }
}

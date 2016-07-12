package fi.ohtu.mobilityprofile.domain;

import com.orm.SugarRecord;

/**
 * Class is used to save route searches the user has made.
 */
public class RouteSearch extends SugarRecord {

    long timestamp;
    String startlocation;
    String destination;
    String nearestknownlocation;

    /**
     *
     */
    public RouteSearch() {

    }

    /**
     * Creates Routesearch.
     * @param timestamp timestamp of the routesearch
     * @param startlocation starting location of the routesearch
     * @param destination destination of the routesearch
     */
    public RouteSearch(long timestamp, String startlocation, String destination) {
        this.timestamp = timestamp;
        this.startlocation = startlocation;
        this.destination = destination;
        this.nearestknownlocation = null;
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

    public void setNearestKnownLocation(String nearestKnownLocation) {
        this.nearestknownlocation = nearestKnownLocation;
    }

    @Override
    public String toString() {
        return "Start: " + startlocation + ". Destination: " + destination;
    }
}

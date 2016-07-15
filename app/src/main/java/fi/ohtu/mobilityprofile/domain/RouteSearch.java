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
    
    Float startlatitude;
    Float startlongitude;
    Float destinationlatitude;
    Float destinationlongitude;

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

    /**
     * Creates Routesearch.
     * @param timestamp timestamp of the routesearch
     * @param startlocation starting location of the routesearch
     * @param destination destination of the routesearch
     * @param startlatitude latitude of startlocation
     * @param startlongitude longitude of startlocation
     * @param destinationlatitude latitude of destination
     * @param destinationlongitude longitude of destination
     */
    public RouteSearch(long timestamp, String startlocation, String destination, Float startlatitude,
                       Float startlongitude, Float destinationlatitude, Float destinationlongitude) {
        this.timestamp = timestamp;
        this.startlocation = startlocation;
        this.destination = destination;
        this.nearestknownlocation = null;
        this.startlatitude = startlatitude;
        this.startlongitude = startlongitude;
        this.destinationlatitude = destinationlatitude;
        this.destinationlongitude = destinationlongitude;
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

    public Float getStartlatitude() {
        return startlatitude;
    }

    public Float getStartlongitude() {
        return startlongitude;
    }

    public Float getDestinationlatitude() {
        return destinationlatitude;
    }

    public Float getDestinationlongitude() {
        return destinationlongitude;
    }

    @Override
    public String toString() {
        return "Start: " + startlocation + ", " +  startlatitude + ", " + startlongitude +
                ". Destination: " + destination + ", " + destinationlatitude + ", " + destinationlongitude;
    }
}

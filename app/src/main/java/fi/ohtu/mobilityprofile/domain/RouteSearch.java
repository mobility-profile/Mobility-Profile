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
    Coordinate startCoordinates;
    Coordinate destinationCoordinates;

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
     * @param startCoordinates coordinates of the starting location
     * @param destinationCoordinates coordinates of the destination
     */
    public RouteSearch(long timestamp, String startlocation, String destination, Coordinate startCoordinates, Coordinate destinationCoordinates) {
        this.timestamp = timestamp;
        this.startlocation = startlocation;
        this.destination = destination;
        this.nearestknownlocation = null;
        this.startCoordinates = startCoordinates;
        this.destinationCoordinates = destinationCoordinates;
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

    public Coordinate getStartCoordinates() {
        return startCoordinates;
    }

    public Coordinate getDestinationCoordinates() {
        return destinationCoordinates;
    }

    @Override
    public String toString() {
        return "Start: " + startlocation + ". Destination: " + destination;
    }
}

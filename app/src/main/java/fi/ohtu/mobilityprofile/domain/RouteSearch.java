package fi.ohtu.mobilityprofile.domain;

import com.orm.SugarRecord;

/**
 * Class is used to save route searches the user has made.
 */
public class RouteSearch extends SugarRecord {

    long timestamp;
    int mode;
    Place startlocation;
    Place destination;

    public RouteSearch() {

    }

    /**
     * Creates Routesearch.
     * @param timestamp     timestamp of the routesearch
     * @param mode          0 for intracity, 1 for intercity
     * @param startlocation starting location of the routesearch
     * @param destination   destination of the routesearch
     */
    public RouteSearch(long timestamp, int mode, Place startlocation, Place destination) {
        this.timestamp = timestamp;
        this.mode = mode;
        this.startlocation = startlocation;
        this.destination = destination;
        this.startlocation.save();
        this.destination.save();
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public Place getStartlocation() {
        return this.startlocation;
    }

    public Place getDestination() {
        return this.destination;
    }

    public Coordinate getStartCoordinates() {
        return this.startlocation.getCoordinate();
    }

    public Coordinate getDestinationCoordinates() {
        return this.getDestination().getCoordinate();
    }

    @Override
    public String toString() {
        return "Start: " + startlocation + ". Destination: " + destination;
    }
}

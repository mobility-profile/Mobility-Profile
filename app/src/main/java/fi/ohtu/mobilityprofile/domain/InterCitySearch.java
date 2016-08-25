package fi.ohtu.mobilityprofile.domain;

import com.orm.SugarRecord;

/**
 * Class is used to save searches between cities the user has made.
 */
public class InterCitySearch extends SugarRecord {
    Place startlocation;
    Place destination;
    long timestamp;

    public InterCitySearch() {
    }

    /**
     * Creates InterCitySearch.
     * @param startlocation starting location of the search
     * @param destination destination of the search
     * @param timestamp the time the search was done
     */
    public InterCitySearch(Place startlocation, Place destination, long timestamp) {
        this.startlocation = startlocation;
        this.destination = destination;
        this.timestamp = timestamp;
    }

    public Place getStartlocation() {
        return startlocation;
    }

    public Place getDestination() {
        return destination;
    }

    public long getTimestamp() {
        return timestamp;
    }
}

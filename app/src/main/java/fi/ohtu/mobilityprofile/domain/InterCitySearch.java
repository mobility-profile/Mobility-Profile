package fi.ohtu.mobilityprofile.domain;

import com.orm.SugarRecord;

/**
 * Class is used to save searches between cities the user has made.
 */
public class InterCitySearch extends SugarRecord {
    String startlocation;
    String destination;
    long timestamp;

    public InterCitySearch() {
    }

    /**
     * Creates InterCitySearch.
     * @param startlocation starting location of the search
     * @param destination destination of the search
     * @param timestamp the time the search was done
     */
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

package fi.ohtu.mobilityprofile.domain;

import com.orm.SugarRecord;

/**
 * Class is used to save visits to SignificantPlaces.
 */
public class Visit extends SugarRecord {
    private long timestamp;
    private SignificantPlace significantPlace;

    /**
     *
     */
    public Visit() {
    }

    /**
     * Creates Visit.
     * @param timestamp timestamp of the place visited
     * @param place SignificantPlace to be referred to
     */
    public Visit(long timestamp, SignificantPlace place) {
        this.timestamp = timestamp;
        this.significantPlace = place;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public SignificantPlace getSignificantPlace() {
        return significantPlace;
    }
}

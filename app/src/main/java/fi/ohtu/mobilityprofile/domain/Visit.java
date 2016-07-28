package fi.ohtu.mobilityprofile.domain;

import com.orm.SugarRecord;

import fi.ohtu.mobilityprofile.data.SignificantPlaceDao;

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
     * @param timestamp timestamp of the visit
     * @param place SignificantPlace to be referred to
     */
    public Visit(long timestamp, SignificantPlace place) {
        this.timestamp = timestamp;
        this.significantPlace = place;
    }

    /**
     * Creates Visit.
     * @param timestamp timestamp of the visit
     */
    public Visit(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public SignificantPlace getSignificantPlace() {
        return significantPlace;
    }

    public String getLocation() {
        return significantPlace.getLocation();
    }

    public void setSignificantPlace(SignificantPlace place) {
        this.significantPlace = place;
    }
}

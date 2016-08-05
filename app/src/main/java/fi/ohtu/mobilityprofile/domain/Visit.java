package fi.ohtu.mobilityprofile.domain;

import com.orm.SugarRecord;

/**
 * Class is used to save visits to SignificantPlaces.
 */
public class Visit extends SugarRecord implements HasCoordinate {
    private long enterTime;
    private long exitTime;
    private SignificantPlace significantPlace;

    /**
     *
     */
    public Visit() {
    }

    /**
     * Creates Visit.
     * @param enterTime enterTime of the visit
     * @param place SignificantPlace to be referred to
     */
    public Visit(long enterTime, long exitTime, SignificantPlace place) {
        this.enterTime = enterTime;
        this.exitTime = exitTime;
        this.significantPlace = place;
    }

    public long getEnterTime() {
        return enterTime;
    }

    public long getExitTime() {
        return this.exitTime;
    }

    public SignificantPlace getSignificantPlace() {
        return significantPlace;
    }

    public String getAddress() {
        return significantPlace.getAddress();
    }

    public void setSignificantPlace(SignificantPlace place) {
        this.significantPlace = place;
    }

    @Override
    public Coordinate getCoordinate() {
        return this.getSignificantPlace().getCoordinate();
    }

    @Override
    public long save() {
        this.significantPlace.save();
        return super.save();
    }

    @Override
    public double distanceTo(HasCoordinate hasCoordinate) {
        return this.getSignificantPlace().getCoordinate().distanceTo(hasCoordinate.getCoordinate());
    }

}

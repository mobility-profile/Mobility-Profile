package fi.ohtu.mobilityprofile.domain;

import com.orm.SugarRecord;

/**
 * Class is used to save visits to Places.
 */
public class Visit extends SugarRecord implements HasCoordinate {
    private long enterTime;
    private long exitTime;
    private Place place;

    /**
     *
     */
    public Visit() {
    }

    /**
     * Creates Visit.
     * @param enterTime enterTime of the visit
     * @param place Place to be referred to
     */
    public Visit(long enterTime, long exitTime, Place place) {
        this.enterTime = enterTime;
        this.exitTime = exitTime;
        this.place = place;
    }

    public long getEnterTime() {
        return enterTime;
    }

    public long getExitTime() {
        return this.exitTime;
    }

    public Place getPlace() {
        return place;
    }

    public String getAddress() {
        return place.getAddress();
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    @Override
    public Coordinate getCoordinate() {
        return this.getPlace().getCoordinate();
    }

    @Override
    public long save() {
        this.place.save();
        return super.save();
    }

    @Override
    public double distanceTo(HasCoordinate hasCoordinate) {
        return this.getPlace().getCoordinate().distanceTo(hasCoordinate.getCoordinate());
    }

}

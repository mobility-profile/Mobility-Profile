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
        this.enterTime = 0;
        this.exitTime = 0;
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

    public void setExitTime(long time) {
        this.exitTime = time;
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
    public double distanceTo(HasCoordinate hasCoordinate) {
        return this.getPlace().getCoordinate().distanceTo(hasCoordinate.getCoordinate());
    }

}

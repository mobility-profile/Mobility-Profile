package fi.ohtu.mobilityprofile.domain;

import android.location.Address;

import com.orm.SugarRecord;

/**
 * Class is used to save visits to Places.
 */
public class Visit extends SugarRecord {
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
     * @param enterTime the time the user came to this location
     * @param exitTime the time the user left this location
     * @param place the Place the user visited
     */
    public Visit(long enterTime, long exitTime, Place place) {
        this.enterTime = enterTime;
        this.exitTime = exitTime;
        this.place = place;
    }

    public long getEnterTime() {
        return this.enterTime;
    }

    public long getExitTime() {
        return this.exitTime;
    }

    public void setExitTime(long time) {
        this.exitTime = time;
    }

    public Place getPlace() {
        return this.place;
    }

    public Address getAddress() {
        return this.place.getAddress();
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    /**
     * Returns the distance between this Visit and given coordinate
     * @param coordinate coordinate to be compared
     * @return distance
     */
    public double distanceTo(Coordinate coordinate) {
        return this.place.getCoordinate().distanceTo(coordinate);
    }
}

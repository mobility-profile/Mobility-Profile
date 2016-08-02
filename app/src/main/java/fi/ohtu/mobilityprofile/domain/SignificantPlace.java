package fi.ohtu.mobilityprofile.domain;

import com.orm.SugarRecord;

/**
 * Class is used to save places assumed significant to the user (ie. places where he/she spends some
 * time and not just points on the road).
 */
public class SignificantPlace extends SugarRecord {
    private String address;
    private Float latitude;
    private Float longitude;
    private boolean favourite;
    private boolean removed;

    /**
     *
     */
    public SignificantPlace() {
    }

    /**
     * Creates SignificantPlace.
     *
     * @param latitude  latitude of the location
     * @param longitude longitude of the location
     */
    public SignificantPlace(String address, Float latitude, Float longitude) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.favourite = false;
        this.removed = false;
    }

    public Float getLongitude() {
        return longitude;
    }

    public Float getLatitude() {
        return latitude;
    }

    public String getLocation() {
        return address;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }
}

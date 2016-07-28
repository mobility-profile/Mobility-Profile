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

    /**
     *
     */
    public SignificantPlace() {
    }

    /**
     * Creates SignificantPlace.
     * @param latitude latitude of the location
     * @param longitude longitude of the location
     */
    public SignificantPlace(Float latitude, Float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public Float getLatitude() {
        return latitude;
    }

    public String getLocation() { return address; }
}

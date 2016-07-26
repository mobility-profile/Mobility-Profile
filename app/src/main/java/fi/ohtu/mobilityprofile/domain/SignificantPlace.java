package fi.ohtu.mobilityprofile.domain;

import android.graphics.PointF;

import com.orm.SugarRecord;

/**
 * Class is used to save locations the user visits frequently.
 */
public class SignificantPlace extends SugarRecord {
    private String location;
    private Float latitude;
    private Float longitude;

    /**
     *
     */
    public SignificantPlace() {
    }

    /**
     * Creates new SignificantPlace
     * @param location location of the significantPlace
     * @param latitude latitude of the location
     * @param longitude longitude of the location
     */
    public SignificantPlace(String location, Float latitude, Float longitude) {
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public Float getLatitude() {
        return latitude;
    }

    public Float getLongitude() {
        return longitude;
    }



}

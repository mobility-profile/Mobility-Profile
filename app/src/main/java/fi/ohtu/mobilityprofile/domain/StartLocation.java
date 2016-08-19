package fi.ohtu.mobilityprofile.domain;

import com.orm.SugarRecord;

/**
 * Class is used to save raw gps data.
 */
public class StartLocation extends GpsPoint {

    /**
     *
     */
    public StartLocation() {
        super();
    }

    /**
     * Creates GpsPoint.
     * @param timestamp timestamp of the visit
     * @param latitude latitude
     * @param longitude longitude
     */
    public StartLocation(long timestamp, float accuracy, Float latitude, Float longitude) {
        super(timestamp, accuracy, latitude, longitude);
    }

}

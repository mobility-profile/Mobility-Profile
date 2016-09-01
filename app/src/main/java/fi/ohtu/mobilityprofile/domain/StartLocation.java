package fi.ohtu.mobilityprofile.domain;

import com.orm.SugarRecord;

/**
 * Class is used to save the starting locations of the searches
 */
public class StartLocation extends GpsPoint {

    /**
     * Creates GpsPoint.
     */
    public StartLocation() {
        super();
    }

    /**
     * Creates GpsPoint.
     * @param timestamp timestamp of the visit
     * @param accuracy accuracy of location
     * @param latitude latitude
     * @param longitude longitude
     */
    public StartLocation(long timestamp, float accuracy, Float latitude, Float longitude) {
        super(timestamp, accuracy, latitude, longitude);
    }

}

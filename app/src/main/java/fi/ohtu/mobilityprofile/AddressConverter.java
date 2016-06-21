package fi.ohtu.mobilityprofile;

import android.graphics.PointF;

public class AddressConverter {

    /**
     * Converts GPS coordinates to an address.
     * @param location
     * @return
     */
    public static String convertToAddress(PointF location) {
        return location.x + " " + location.y;
        // TODO: Do the actual convert from a gps location to an address.
    }
}

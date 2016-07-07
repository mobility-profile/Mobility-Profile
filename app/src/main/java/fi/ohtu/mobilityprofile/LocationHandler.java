package fi.ohtu.mobilityprofile;

import android.location.Location;
import android.util.Log;

public class LocationHandler {

    private static Location location;

    /**
     *
     * @return
     */
    public static Location getLocation() {
        Log.e("LOCATIONHANDLER", "get location " + location );
        return location;
    }

    /**
     *
     * @param location
     */
    public static void setLocation(Location location) {
        if (location != null) {
            LocationHandler.location = location;
            Log.e("LOCATIONHANDLER", "set location " + location );
        }
    }
}

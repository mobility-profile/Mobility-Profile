package fi.ohtu.mobilityprofile;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * This class gives information about the user permissions.
 */
public class PermissionManager {

    private static boolean ACCESS_TO_FINE_LOCATION;
    private static boolean READ_CALENDAR;

    /**
     * Gives the state of the fine location permission.
     * @return true/false
     */
    public static boolean permissionToFineLocation() {
        return ACCESS_TO_FINE_LOCATION;
    }

    /**
     * Gives the state of the read calendar permission.
     * @return true/false
     */
    public static boolean permissionToReadCalendar() {
        return READ_CALENDAR;
    }

    public static void setPermissions(Context context) {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ACCESS_TO_FINE_LOCATION = false;
        } else {
            ACCESS_TO_FINE_LOCATION = true;
        }

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            READ_CALENDAR = false;
        } else {
            READ_CALENDAR = true;
        }
    }
}

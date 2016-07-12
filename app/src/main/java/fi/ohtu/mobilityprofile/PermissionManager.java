package fi.ohtu.mobilityprofile;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * This class gives information about the user permissions.
 */
public class PermissionManager {

    /**
     * Gives the state of the fine location permission.
     * @param context context
     * @return true/false
     */
    public static boolean permissionToFineLocation(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Gives the state of the read calendar permission.
     * @param context context
     * @return true/false
     */
    public static boolean permissionToReadCalendar(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED;
    }
}

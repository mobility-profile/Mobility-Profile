package fi.ohtu.mobilityprofile.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import fi.ohtu.mobilityprofile.MainActivity;

/**
 * This class gives information about the user permissions.
 */
public class PermissionManager {

    /**
     * Gives the state of the fine location permission.
     * @return true/false
     */
    public static boolean permissionToFineLocation() {
        return ContextCompat.checkSelfPermission(MainActivity.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Gives the state of the read calendar permission.
     * @return true/false
     */
    public static boolean permissionToReadCalendar() {
        return ContextCompat.checkSelfPermission(MainActivity.getContext(), Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Gives the state of the write external storage permission.
     * @return true/false
     */
    public static boolean permissionToWriteExternalStorage() {
        return ContextCompat.checkSelfPermission(MainActivity.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
}

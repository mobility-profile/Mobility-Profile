package fi.ohtu.mobilityprofile.remoteconnection;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;

import com.commonsware.cwac.security.PermissionLint;
import com.commonsware.cwac.security.PermissionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SecurityCheck {
    public static boolean securityCheckOk(SharedPreferences sharedPreferences) {
        return sharedPreferences.getBoolean("securitycheck", false);
    }

    public static boolean doSecurityCheck(Context context, SharedPreferences sharedPreferences) {
        if (isSecureConnection(context)) {
            sharedPreferences.edit().putBoolean("securitycheck", true).apply();
            return true;
        }

        return false;
    }

    public static List<PackageInfo> getConflictInfo(Context context) {
        List<PackageInfo> conflictApps = new ArrayList<>();

        Map<PackageInfo, ArrayList<PermissionLint>> customPermissions = PermissionUtils.checkCustomPermissions(context);

        for (PackageInfo packageInfo : customPermissions.keySet()) {
            for (PermissionLint permissionLint : customPermissions.get(packageInfo)) {
                if (hasPermissionConflict(permissionLint)) {
                    conflictApps.add(packageInfo);
                }
            }
        }

        return conflictApps;
    }

    private static boolean isSecureConnection(Context context) {
        return getConflictInfo(context).isEmpty();
    }

    private static boolean hasPermissionConflict(PermissionLint permissionLint) {
        return permissionLint.wasDowngraded || permissionLint.signaturesDiffer
                || permissionLint.wasUpgraded || permissionLint.proseDiffers;
    }
}

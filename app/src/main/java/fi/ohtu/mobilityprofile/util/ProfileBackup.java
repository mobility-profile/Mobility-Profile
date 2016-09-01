package fi.ohtu.mobilityprofile.util;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import fi.ohtu.mobilityprofile.MainActivity;

/**
 * Class is used to export and import Mobility Profile's database.
 */
public class ProfileBackup {
    private final String DB_NAME = "mobilityprofile.db";


    /**
     * Backs up the database to the device or imports it from the device.
     * @param procedure "back up" or "import" in String
     */
    public void handleBackup(String procedure) {
        final Context context = MainActivity.getContext();

        if (!checkPermissionToWriteAndRead()) {
            return;
        }

        Boolean sdAvailable = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (!sdAvailable) {
            Toast.makeText(context, "Failed to " + procedure + " data. There is no SD card available.", Toast.LENGTH_LONG).show();
            return;
        }
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        String currentDBPath;
        String backupDBPath;
        File currentDB = null;
        File backupDB = null;

        switch (procedure) {
            case "back up":
                currentDBPath = "/data/" + context.getPackageName() + "/databases/" + DB_NAME;
                backupDBPath = DB_NAME;
                currentDB = new File(data, currentDBPath);
                backupDB = new File(sd, backupDBPath);
                break;
            case "import":
                currentDBPath = DB_NAME;
                backupDBPath = "/data/" + context.getPackageName() + "/databases/" + DB_NAME;
                currentDB = new File(sd, currentDBPath);
                backupDB = new File(data, backupDBPath);
                if (!currentDB.exists()) {
                    Toast.makeText(context, "No database backup file was found.", Toast.LENGTH_LONG).show();
                    return;
                }
                break;
        }

        try {
            FileChannel source = new FileInputStream(currentDB).getChannel();
            FileChannel destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(context, "Succeeded to " + procedure + " data!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            Toast.makeText(context, "Failed to " + procedure + " data.", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Checks if the app has permission to write and read to/from external storage.
     * @return true if permission is granted, false if not
     */
    private Boolean checkPermissionToWriteAndRead() {
        if (!PermissionManager.permissionToWriteExternalStorage()) {
            ActivityCompat.requestPermissions((Activity) MainActivity.getContext(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return PermissionManager.permissionToWriteExternalStorage();
        }
        return true;
    }

}

package fi.ohtu.mobilityprofile;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * Class is used to export and import Mobility Profile's database.
 */
public class ProfileBackup {
    private final String DB_NAME = "mobilityprofile.db";
    private Context context;

    /**
     * Creates ProfileBackup.
     * @param context context used for creating Toast messages
     */
    public ProfileBackup(Context context) {
        this.context = context;
    }
    /**
     * Makes a copy of the database and saves it in SD card.
     */
    public void exportDatabase() {
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        String currentDBPath = "/data/"+ "com.authorwjf.sqliteexport" +"/databases/" + DB_NAME;
        String backupDBPath = DB_NAME;
        File currentDB = new File(data, currentDBPath);
        File backUpDB = new File(sd, backupDBPath);

        try {
            FileChannel source = new FileInputStream(currentDB).getChannel();
            FileChannel destination = new FileOutputStream(backUpDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Imports an existing Mobility Profile database.
     */
    public void importDatabase() {

    }

}

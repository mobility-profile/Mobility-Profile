package fi.ohtu.mobilityprofile;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to get events from calendars.
 */
public class CalendarConnection {
    private String[] EVENT_PROJECTION = new String[]{
            CalendarContract.Events.EVENT_LOCATION,
            CalendarContract.Instances.BEGIN,
            CalendarContract.Events.ALL_DAY
    };

    private final long HOUR = 3600 * 1000;
    private Context activity;
    private List<String> locations;

    /**
     * Constructor of the class CalendarConnection.
     * @param context Context of the calling app.
     */
    public CalendarConnection(Context context) {
        this.activity = context;
        locations = new ArrayList<>();
        query();
    }

    /**
     * Queries events from all calendars on the user's device. Selects only events that take place
     * on the same day and/or within 3 hours.
     * @throws SecurityException
     */
    private void query() throws SecurityException {

        Cursor cursor = null;
        ContentResolver cr = activity.getContentResolver();
        Uri eventUri = buildUri();

        cursor = cr.query(eventUri, EVENT_PROJECTION, null, null, null);

        while (cursor.moveToNext()) {
            locations.add(cursor.getString(0));
        }
        cursor.close();
    }

    /**
     * Builds the uri to query events taking place between current time and 3 hours later.
     * @return uri referring to calendar api
     */
    private Uri buildUri() {
        long beginTime = System.currentTimeMillis();
        long endTime = beginTime + 3 * HOUR;

        Uri.Builder eventsUriBuilder = CalendarContract.Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(eventsUriBuilder, beginTime);
        ContentUris.appendId(eventsUriBuilder, endTime);
        Uri eventUri = eventsUriBuilder.build();

        return eventUri;
    }

    public List<String> getLocations() {
        return this.locations;
    }
}

package fi.ohtu.mobilityprofile.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Class is used to get events from calendars.
 */
public class CalendarConnection {

    // Projection is used to choose locations and beginning times of calendar events, and info about whether
    // they are all day events or not.
    private String[] EVENT_PROJECTION = new String[]{
        CalendarContract.Events.EVENT_LOCATION,
        CalendarContract.Instances.BEGIN,
        CalendarContract.Events.ALL_DAY
    };

    private Context context;
    private List<String> eventLocations;
    private List<String> allDayEventLocations;
    private static final int HOUR = 3600 * 1000;
    private static final int LOCATION = 0;
    private static final int ALL_DAY = 1;


    /**
     * Constructor of the class CalendarConnection.
     * @param context Context of the calling app.
     */
    public CalendarConnection(Context context) {
        this.context = context;

        this.eventLocations = new ArrayList<>();
        this.allDayEventLocations = new ArrayList<>();

        queryEvents();
    }

    /**
     * Queries events from all calendars on the user's device.
     */
    private void queryEvents() {
        Cursor cursor;
        ContentResolver cr = context.getContentResolver();
        Uri eventUri = buildUri();

        cursor = cr.query(eventUri, EVENT_PROJECTION, null, null, null);

        if (cursor != null) {
            getLocations(cursor);
            cursor.close();
        }
    }

    /**
     * Builds the uri to query events taking place between current time and 3 hours later.
     * @return uri referring to calendar api.
     */
    private Uri buildUri() {
        long now = System.currentTimeMillis();
        long endTime = now + 3 * HOUR;

        Uri.Builder eventsUriBuilder = CalendarContract.Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(eventsUriBuilder, now);
        ContentUris.appendId(eventsUriBuilder, endTime);

        return eventsUriBuilder.build();
    }

    /**
     * Adds all valid locations to a list from the results of the query.
     * @param cursor Pointer to the results of the query.
     */
    private void getLocations(Cursor cursor) {
        while (cursor.moveToNext()) {
            String location = cursor.getString(LOCATION);
            if (location != null) {
                if (cursor.getString(ALL_DAY).equals("1469491200000")) {
                    allDayEventLocations.add(location);
                } else {
                    eventLocations.add(location);
                }
            }
        }
    }

    /**
     * Returns locations queried from calendar.
     *
     * @return locations of the events
     */
    public List<String> getEventLocations() {
        return eventLocations;
    }

    /**
     * Returns all day event locations queried from calendar.
     *
     * @return locations of the events
     */
    public List<String> getAllDayEventLocations() {
        return allDayEventLocations;
    }
}
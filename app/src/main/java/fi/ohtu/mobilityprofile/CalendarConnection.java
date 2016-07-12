package fi.ohtu.mobilityprofile;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

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
    private String eventLocation;
    private String allDayEventLocation;
    private static final int HOUR = 3600 * 1000;
    private static final int LOCATION = 0;
    private static final int ALL_DAY = 1;


    /**
     * Constructor of the class CalendarConnection.
     * @param context Context of the calling app.
     */
    public CalendarConnection(Context context) {
        this.context = context;
        queryEvents();
    }

    /**
     * Queries events from all calendars on the user's device.
     */
    private void queryEvents() {
        Cursor cursor = null;
        ContentResolver cr = context.getContentResolver();
        Uri eventUri = buildUri();

        cursor = cr.query(eventUri, EVENT_PROJECTION, null, null, null);

        if (cursor != null) {
            getLocation(cursor);

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
        Uri eventUri = eventsUriBuilder.build();

        return eventUri;
    }

    /**
     * Finds the first valid location from the results of the query.
     * @param cursor Pointer to the results of the query.
     */
    private void getLocation(Cursor cursor) {
        while (cursor.moveToNext()) {
            String location = cursor.getString(LOCATION);
            if (location != null) {
                if (cursor.getString(ALL_DAY) == "1") {
                    allDayEventLocation = location;
                } else {
                    eventLocation = location;
                    break;
                }
            }
        }
    }

    /**
     * Returns location queried from calendar. If null, location of an all day event is returned.
     * @return location of the event
     */
    public String getEventLocation() {
        return this.eventLocation != null ? eventLocation : allDayEventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }
}

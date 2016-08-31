package fi.ohtu.mobilityprofile.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.telephony.TelephonyManager;

import java.util.ArrayList;
import java.util.List;

import fi.ohtu.mobilityprofile.domain.Coordinate;
import fi.ohtu.mobilityprofile.suggestions.Suggestion;
import fi.ohtu.mobilityprofile.suggestions.SuggestionAccuracy;
import fi.ohtu.mobilityprofile.suggestions.SuggestionSource;

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
    private List<Suggestion> eventLocations;
    private List<Suggestion> allDayEventLocations;
    private static final int HOUR = 3600 * 1000;
    private static final int LOCATION = 0;
    private static final int ALL_DAY = 1;


    /**
     * Constructor of the class CalendarConnection.
     *
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
     *
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
     *
     * @param cursor Pointer to the results of the query.
     */
    private void getLocations(Cursor cursor) {
        while (cursor.moveToNext()) {
            String location = cursor.getString(LOCATION);

            if (location == null) continue;

            Coordinate coordinate = AddressConverter.getCoordinatesFromAddress(context, location);
            if (coordinate == null) continue;

            // Check if the user is in the same country as the geocoded address.
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String countryCode = tm.getSimCountryIso();
            if (!countryCode.equalsIgnoreCase(AddressConverter.getCountryCode(context, location))) continue;

//            Suggestion suggestion = new Suggestion(location, SuggestionAccuracy.VERY_HIGH, SuggestionSource.CALENDAR_SUGGESTION, coordinate);

//            if (cursor.getString(ALL_DAY).equals("1")) {
//                allDayEventLocations.add(suggestion);
//            } else {
//                eventLocations.add(suggestion);
//            }
       }
    }

    /**
     * Returns locations queried from calendar.
     *
     * @return locations of the events
     */
    public List<Suggestion> getEventLocations() {
        return eventLocations;
    }

    /**
     * Returns all day event locations queried from calendar.
     *
     * @return locations of the events
     */
    public List<Suggestion> getAllDayEventLocations() {
        return allDayEventLocations;
    }
}

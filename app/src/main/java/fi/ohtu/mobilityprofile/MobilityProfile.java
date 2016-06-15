package fi.ohtu.mobilityprofile;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import fi.ohtu.mobilityprofile.data.CalendarTag;
import fi.ohtu.mobilityprofile.data.CalendarTagDao;

/**
 * This class is used for calculating the most likely trips the user is going to make.
 */
public class MobilityProfile {

    private List<String> eventLocations = new ArrayList<>();

    private Context context;
    private String latestGivenDestination;
    private boolean calendarDestination;
    private String nextLocation;

    /**
     * Constructor of class MobilityProfile.
     * @param context Context of the calling app. Used when getting events from calendars.
     */
    public MobilityProfile(Context context) {
        this.context = context;
    }

    public MobilityProfile() {

    }
    /**
     * Returns the most probable destination, when the user is in startLocation.
     *
     * @param startLocation Location where the user is starting
     * @return Most probable destination
     */
    public String getMostLikelyDestination(String startLocation) {
        // TODO: Add some logic.

        nextLocation = "Kumpula";
        latestGivenDestination = nextLocation;

        calendarDestination = false;

        getLocationFromCalendar();

        return nextLocation;
    }


    /**
     * Selects location from calendar events
     */
    private void getLocationFromCalendar() {
        CalendarConnection cc = new CalendarConnection(context);
        eventLocations = cc.getLocations();

        if (eventLocations.size() > 0) {
            getNextValidLocation();

            latestGivenDestination = nextLocation;
            calendarDestination = true;

            CalendarTag calendarTag = CalendarTagDao.findTheMostUsedTag(nextLocation);
            if (calendarTag != null) {
                nextLocation = calendarTag.getValue();
            }
        }
    }

    /**
     * Selects the first valid location from the event location list
     */
    private void getNextValidLocation() {
        for (String location : eventLocations) {
            if (!location.equals("null")) {
                    nextLocation = location;
                    break;
            }
        }
    }

    public void setCalendarEventList(ArrayList<String> events) {
        this.eventLocations = events;
    }

    public String getLatestGivenDestination() {
        return latestGivenDestination;
    }

    public boolean isCalendarDestination() {
        return calendarDestination;
    }
}

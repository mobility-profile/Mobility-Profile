package fi.ohtu.mobilityprofile;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fi.ohtu.mobilityprofile.data.CalendarTag;
import fi.ohtu.mobilityprofile.data.CalendarTagDao;

/**
 * This class is used for calculating the most likely trips the user is going to make.
 */
public class MobilityProfile {

    private List<String> calendarEvents = new ArrayList<>();

    private String latestGivenDestination;
    private boolean calendarDestination;
    private String nextLocation;

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
     * Returns the first location queried from the calendar
     * @return Location queried from the calendar or default location
     */
    public void getLocationFromCalendar() {
        if (calendarEvents.size() > 0) {

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
     *
     */
    private void getNextValidLocation() {
        for (int i = 0; i < calendarEvents.size(); i++) {

            nextLocation = extractLocationFromEventString(calendarEvents.get(i));

            if (!nextLocation.equals("null")) {
                break;
            }
        }
    }

    /**
     * Extracts location from the event string
     * @param event Event queried from the calendar
     * @return location of the event
     */
    private String extractLocationFromEventString(String event) {
        String location = event.split("%")[0];
        return location;
    }



    public String getLatestGivenDestination() {
        return latestGivenDestination;
    }

    public boolean isCalendarDestination() {
        return calendarDestination;
    }
}

package fi.ohtu.mobilityprofile;


import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import fi.ohtu.mobilityprofile.data.CalendarTagDao;

/**
 * This class is used for calculating the most likely trips the user is going to make.
 */
public class MobilityProfile {

    private List<String> calendarEvents = new ArrayList<>();

    /**
     * Returns the most probable destination, when the user is in startLocation.
     *
     * @param startLocation Location where the user is starting
     * @return Most probable destination
     */
    public String getMostLikelyDestination(String startLocation) {
        // TODO: Add some logic.

        String nextLocation = "Kumpula";

        if (calendarEvents.size() > 0) {
           nextLocation = extractLocation(calendarEvents.get(0));
        }

        return nextLocation;
    }

    public void setCalendarEvents(List<String> events) {
        this.calendarEvents = events;
    }

    private String extractLocation(String event) {
        String location = event.split("%")[0];
        return location;
    }
}

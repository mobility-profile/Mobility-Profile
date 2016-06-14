package fi.ohtu.mobilityprofile;

import java.util.ArrayList;
import java.util.List;

import fi.ohtu.mobilityprofile.data.CalendarTag;
import fi.ohtu.mobilityprofile.data.CalendarTagDao;
import fi.ohtu.mobilityprofile.data.Visit;
import fi.ohtu.mobilityprofile.data.VisitDao;

/**
 * This class is used for calculating the most likely trips the user is going to make.
 */
public class MobilityProfile {

    private CalendarTagDao calendarTagDao;
    private VisitDao visitDao;

    private List<String> calendarEvents = new ArrayList<>();

    private String latestGivenDestination;
    private boolean calendarDestination;
    private String nextLocation;

    public MobilityProfile(CalendarTagDao calendarTagDao, VisitDao visitDao) {
        this.calendarTagDao = calendarTagDao;
        this.visitDao = visitDao;
    }

    /**
     * Returns the most probable destination, when the user is in startLocation.
     *
     * @param startLocation Location where the user is starting
     * @return Most probable destination
     */
    public String getMostLikelyDestination(String startLocation) {

        getLocationFromDatabase(startLocation);
        latestGivenDestination = nextLocation;

        calendarDestination = false;
        getLocationFromCalendar();

        return nextLocation;
    }

    /**
     * Finds all the visits where location is the startLocation
     * and then decides the most likely next destination of them
     */
    private void getLocationFromDatabase(String startLocation) {
        List<Visit> visits = visitDao.getVisitsByLocation(startLocation);
        if (visits.isEmpty()) {
            // TODO: Something sensible
            nextLocation = "home";
        } else {
            // TODO: Add some logic.
            nextLocation = visits.get(0).getLocation();

        }
    }


    /**
     * Returns the first location queried from the calendar
     * @return Location queried from the calendar or default location
     */
    private void getLocationFromCalendar() {
        if (calendarEvents.size() > 0) {

            getNextValidLocation();

            latestGivenDestination = nextLocation;
            calendarDestination = true;

            CalendarTag calendarTag = calendarTagDao.findTheMostUsedTag(nextLocation);
            if (calendarTag != null) {
                nextLocation = calendarTag.getValue();
            }
        }
    }

    /**
     * Get the first event from the list that has a valid location
     */
    private void getNextValidLocation() {
        for (int i = 0; i < calendarEvents.size(); i++) {
            String location = extractLocationFromEventString(calendarEvents.get(i));
            if (!location.equals("null")) {
                nextLocation = location;
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

    public void setCalendarEventList(ArrayList<String> events) {
        this.calendarEvents = events;
    }

    public String getLatestGivenDestination() {
        return latestGivenDestination;
    }

    public boolean isCalendarDestination() {
        return calendarDestination;
    }
}

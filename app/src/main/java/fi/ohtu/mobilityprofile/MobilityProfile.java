package fi.ohtu.mobilityprofile;

import android.content.Context;

import java.util.List;

import fi.ohtu.mobilityprofile.data.CalendarTag;
import fi.ohtu.mobilityprofile.data.CalendarTagDao;
import fi.ohtu.mobilityprofile.data.RouteSearch;
import fi.ohtu.mobilityprofile.data.RouteSearchDao;
import fi.ohtu.mobilityprofile.data.Visit;
import fi.ohtu.mobilityprofile.data.VisitDao;

/**
 * This class is used for calculating the most likely trips the user is going to make.
 */
public class MobilityProfile {

    private CalendarTagDao calendarTagDao;
    private VisitDao visitDao;
    private RouteSearchDao routeSearchDao;

    private Context context;
    private String latestGivenDestination;
    private boolean calendarDestination;
    private String nextLocation;
    private String eventLocation;

    private List<Visit> visits;
    private List<RouteSearch> routes;

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
     * Creates the MobilityProfile.
     *
     * @param calendarTagDao DAO for calendar tags
     * @param visitDao DAO for visits
     */
    public MobilityProfile(CalendarTagDao calendarTagDao, VisitDao visitDao, RouteSearchDao routeSearchDao) {
        this.calendarTagDao = calendarTagDao;
        this.visitDao = visitDao;
        this.routeSearchDao = routeSearchDao;
    }

    /**
     * Returns the most probable destination, when the user is in startLocation.
     *
     * @param startLocation Location where the user is starting
     * @return Most probable destination
     */
    public String getMostLikelyDestination(String startLocation) {
        calendarDestination = false;
        getLocationFromCalendar();

        if (calendarDestination == false) {
            getLocationFromDatabase(startLocation);
        }

        latestGivenDestination = nextLocation;

        return nextLocation;
    }

    /**
     * Finds all the visits where location is the startLocation
     * and then decides the most likely next destination of them.
     *
     * @param startLocation Starting location
     */
    private void getLocationFromDatabase(String startLocation) {
        // TODO: Use routesearchdao also

        routes = routeSearchDao.getRouteSearchesByStartlocation(startLocation);
        searchFromUsedRoutes();

        visits = visitDao.getVisitsByLocation(startLocation);
        searchFromPreviousVisits();

        if (visits.isEmpty() && routes.isEmpty()) {
            // TODO: Something sensible
            nextLocation = "home";
        } else {
            // TODO: Add some logic.
            nextLocation = visits.get(0).getLocation();

        }
    }


    /**
     * Gets the most probable location from the calendar
     */
    private void getLocationFromCalendar() {
        CalendarConnection cc = new CalendarConnection(context);
        eventLocation = cc.getEventLocation();

        if (eventLocation != null) {
            nextLocation = eventLocation;
            calendarDestination = true;

            CalendarTag calendarTag = calendarTagDao.findTheMostUsedTag(nextLocation);
            if (calendarTag != null) {
                nextLocation = calendarTag.getValue();
            }
        }
    }

    /**
     * Selects location based on previously used routes.
     */
    private void searchFromUsedRoutes() {

    }

    /**
     * Selects location based on previous visits.
     */
    private void searchFromPreviousVisits() {

    }

    /**
     * Saves a calendar event.
     *
     * @param event an events
     */
    public void setCalendarEventLocation(String event) {
        this.eventLocation = event;
    }

    /**
     * Returns the latest destination that was sent to the client.
     *
     * @return Latest given destination
     */
    public String getLatestGivenDestination() {
        return latestGivenDestination;
    }

    /**
     * Tells if the latest given location was retrieved from the calendar.
     *
     * @return True if the location was from calendar, false otherwise
     */
    public boolean isCalendarDestination() {
        return calendarDestination;
    }
}

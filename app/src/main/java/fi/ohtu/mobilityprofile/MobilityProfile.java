package fi.ohtu.mobilityprofile;

import android.content.Context;

import java.sql.Time;
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
    private boolean routeDestination;
    private String startLocation;
    private String nextLocation;
    private String eventLocation;
    private Time currentTime;

    private List<Visit> visits;
    private List<RouteSearch> routes;

    /**
     * Constructor of class MobilityProfile.
     * @param context Context of the calling app. Used when getting events from calendars.
     */
    public MobilityProfile(Context context) {
        this.context = context;
    }


    /**
     * Creates the MobilityProfile.
     *
     * @param calendarTagDao DAO for calendar tags
     * @param visitDao DAO for visits
     * @param routeSearchDao DAO for routeSearch
     */
    public MobilityProfile(Context context, CalendarTagDao calendarTagDao, VisitDao visitDao, RouteSearchDao routeSearchDao) {
        this.context = context;
        this.calendarTagDao = calendarTagDao;
        this.visitDao = visitDao;
        this.routeSearchDao = routeSearchDao;
    }

    /**
     * Creates the MobilityProfile.
     *
     * @param calendarTagDao DAO for calendar tags
     * @param visitDao DAO for visits
     * @param routeSearchDao DAO for routeSearch
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
        this.startLocation = startLocation;
        calendarDestination = false;

        getLocationFromCalendar();
        if (!calendarDestination) {
            getLocationFromDatabase();
        }

        latestGivenDestination = nextLocation;
        return nextLocation;
    }

    /**
     * Finds all the used routes and previous visits where location is the startLocation
     * and then decides the most likely next destination of them.
     */
    private void getLocationFromDatabase() {
        routeDestination = false;
        currentTime = new Time(System.currentTimeMillis());

        searchFromUsedRoutes();
        if (!routeDestination) {
            searchFromPreviousVisits();
        }

        if (visits.isEmpty() && routes.isEmpty()) {
           // TODO: Something sensible
            nextLocation = "home";
        } else {
            // TODO: Add some logic.
            nextLocation = AddressConverter.convertToAddress(visits.get(0).getNearestKnownLocation().getLocation());
        }
    }

    /**
     * Gets the most probable destination from the calendar
     */
    private void getLocationFromCalendar() {
        CalendarConnection calendar = new CalendarConnection(context);
        eventLocation = calendar.getEventLocation();

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
     * Selects destination based on previously used routes.
     */
    private void searchFromUsedRoutes() {
        routes = routeSearchDao.getRouteSearchesByStartlocation(startLocation);
        if (routes != null) {
            searchForPreviouslyUsedRouteAtTheSameTime();
        }
    }

    /**
     * Checks if the user has gone to some destination at the same time in the past.
     * Searches from previously used routes.
     */
    private void searchForPreviouslyUsedRouteAtTheSameTime() {
        for (RouteSearch route : routes) {
            if (aroundTheSameTime(new Time(route.getTimestamp()))) {
                nextLocation = route.getDestination();
                routeDestination = true;
                break;
            }
        }
    }

    /**
     * Checks if selected route was used around the same time in the past, max 2 hours earlier
     * or max 2 hours later than current time.
     * @param routeTime timestamp of the route
     * @return true if route was used within the time frame, false if not.
     */
    private boolean aroundTheSameTime(Time routeTime) {
        int routeHour = routeTime.getHours();
        int routeMin = routeTime.getMinutes();
        int currentHour = currentTime.getHours();
        int currentMin = currentTime.getMinutes();

        if ((routeHour > currentHour - 2 || (routeHour == currentHour - 2 && routeMin >= currentMin))
                && (routeHour < currentHour + 2 || (routeHour == currentHour + 2 && routeMin <= currentMin))) {
            return true;
        }
        return false;
    }

    /**
     * Selects destination based on previous visits.
     */
    private void searchFromPreviousVisits() {
        visits = visitDao.getVisitsByLocation(startLocation);
        if (visits != null) {

        }
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

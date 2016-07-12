package fi.ohtu.mobilityprofile;

import android.content.Context;

import java.sql.Time;
import java.util.Date;
import java.util.List;

import fi.ohtu.mobilityprofile.data.*;
import fi.ohtu.mobilityprofile.domain.CalendarTag;
import fi.ohtu.mobilityprofile.domain.FavouritePlace;
import fi.ohtu.mobilityprofile.domain.RouteSearch;
import fi.ohtu.mobilityprofile.domain.Visit;

/**
 * This class is used for calculating the most likely trips the user is going to make.
 */
public class MobilityProfile {
    public static final int DEFAULT_SUGGESTION = 0;
    public static final int CALENDAR_SUGGESTION = 1;
    public static final int ROUTES_SUGGESTION = 2;
    public static final int VISITS_SUGGESTION = 3;
    public static final int FAVORITES_SUGGESTION = 4;

    private CalendarTagDao calendarTagDao;
    private VisitDao visitDao;
    private RouteSearchDao routeSearchDao;
    private FavouritePlaceDao favouritePlaceDao;

    private CalendarConnection calendar;

    private String latestStartLocation;
    private String latestDestination;

    private int suggestionSource;

    /**
     * Creates the MobilityProfile.
     *
     * @param context        Context of the calling app. Used when getting events from calendars.
     * @param calendarTagDao DAO for calendar tags
     * @param visitDao       DAO for visits
     * @param routeSearchDao DAO for used searches
     */
    public MobilityProfile(Context context, CalendarTagDao calendarTagDao, VisitDao visitDao,
                           RouteSearchDao routeSearchDao, FavouritePlaceDao favouritePlaceDao) {
        this.calendarTagDao = calendarTagDao;
        this.visitDao = visitDao;
        this.routeSearchDao = routeSearchDao;
        this.favouritePlaceDao = favouritePlaceDao;

        this.calendar = new CalendarConnection(context);
    }

    /**
     * Returns the most probable destination, when the user is in startLocation.
     *
     * The algorithm will first check if there are marked events in the calendar within a few hours.
     * If none was found, it will then check places where the user has previously visited.
     * If that data is not available, previously used searches are suggested.
     * Lastly, it will check saved favorite location and suggest one of those.
     *
     * TODO: the algorithm shouldn't always take the first decent suggestion.
     * It should instead compare suggestions from all the different sources and take the best
     * one of those.
     *
     * @param startLocation Location where the user is starting
     * @return Most probable destination
     */
    public String getMostLikelyDestination(String startLocation) {
        this.latestStartLocation = startLocation;
        String nextDestination;

        String calendarSuggestion = searchFromCalendar();
        String visitsSuggestion = searchFromPreviousVisits();
        String routesSuggestion = searchFromUsedRoutes(startLocation);
        String favoritesSuggestion = searchFromFavorites();

        if (calendarSuggestion != null) {
            nextDestination = calendarSuggestion;
            suggestionSource = CALENDAR_SUGGESTION;
        }
        else if (visitsSuggestion != null) {
            nextDestination = visitsSuggestion;
            suggestionSource = VISITS_SUGGESTION;
        }
        else if (routesSuggestion != null) {
            nextDestination = routesSuggestion;
            suggestionSource = ROUTES_SUGGESTION;
        }
        else if (favoritesSuggestion != null) {
            nextDestination = favoritesSuggestion;
            suggestionSource = FAVORITES_SUGGESTION;
        }
        else {
            nextDestination = "Home";
            suggestionSource = DEFAULT_SUGGESTION;
        }

        latestDestination = nextDestination;
        return nextDestination;
    }

    /**
     * Returns the most probable destination from the calendar.
     *
     * @return Destination from calendar
     */
    private String searchFromCalendar() {
        String eventLocation = calendar.getEventLocation();

        if (eventLocation != null) {
            CalendarTag calendarTag = calendarTagDao.findTheMostUsedTag(eventLocation);
            if (calendarTag != null) {
                eventLocation = calendarTag.getValue();
            }
        }

        return eventLocation;
    }

    /**
     * Selects destination based on previously used routes.
     * Checks if the user has gone to some destination at the same time in the past,
     * max 2 hours earlier or max 2 hours later than current time.
     * Searches from previously used routes.
     *
     * @param startLocation Starting location
     * @return Previously used destination
     *
     */
    private String searchFromUsedRoutes(String startLocation) {
        List<RouteSearch> routes = routeSearchDao.getRouteSearchesByStartlocation(startLocation);

        for (RouteSearch route : routes) {
            if (aroundTheSameTime(new Time(route.getTimestamp()), 2, 2)) {
                return route.getDestination();
            }
        }

        return null;
    }

    /**
     * Checks if the selected location was visited or set as destination around the same time in the past,
     * max x hours earlier or max y hours later than current time.
     *
     * @param visitTime    timestamp of the location
     * @param hoursEarlier hours earlier than current time
     * @param hoursLater   hours later than current time
     * @return true if location was visited within the time frame, false if not.
     */
    private boolean aroundTheSameTime(Time visitTime, int hoursEarlier, int hoursLater) {
        Date currentTime = new Date(System.currentTimeMillis());

        int visitHour = visitTime.getHours();
        int visitMin = visitTime.getMinutes();
        int currentHour = currentTime.getHours();
        int currentMin = currentTime.getMinutes();

        if ((visitHour > currentHour - hoursEarlier || (visitHour == currentHour - hoursEarlier && visitMin >= currentMin))
                && (visitHour < currentHour + hoursLater || (visitHour == currentHour + hoursLater && visitMin <= currentMin))) {
            return true;
        }
        return false;
    }

    /**
     * Selects destination based on previous visits.
     * Checks if the user has visited some location around the same time in the past,
     * max 1 hour earlier or max 3 hours later than current time.
     * Searches from visits.
     *
     * @return Previously visited place
     */
    private String searchFromPreviousVisits() {
        List<Visit> visits = visitDao.getAllVisits();

        for (Visit visit : visits) {
            if (aroundTheSameTime(new Time(visit.getTimestamp()), 1, 3)) {
                return visit.getOriginalLocation();
            }
        }

        return null;
    }

    /**
     * Returns the fist saved favorite place.
     *
     * @return First favorite place
     */
    private String searchFromFavorites() {
        List<FavouritePlace> favouritePlaces = favouritePlaceDao.getAllFavouritePlaces();

        return favouritePlaces.isEmpty() ? null : favouritePlaces.get(0).getAddress();
        // TODO: Add some logic to choosing from favorite places.
        // E.g. add a counter that increases every time user uses the favorite place.
    }

    /**
     * Saves a calendar event.
     *
     * @param event an event
     */
    public void setCalendarEventLocation(String event) {
        calendar.setEventLocation(event);
    }

    /**
     * Returns the latest destination that was sent to the client.
     *
     * @return Latest given destination
     */
    public String getLatestDestination() {
        return latestDestination;
    }

    /**
     * Tells if the latest given location was retrieved from the calendar.
     *
     * @return True if the location was from calendar, false otherwise
     */
    public boolean isCalendarDestination() {
        return suggestionSource == CALENDAR_SUGGESTION;
    }
}

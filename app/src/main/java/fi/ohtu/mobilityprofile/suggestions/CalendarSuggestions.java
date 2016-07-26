package fi.ohtu.mobilityprofile.suggestions;

import java.util.ArrayList;
import java.util.List;

import fi.ohtu.mobilityprofile.CalendarConnection;
import fi.ohtu.mobilityprofile.data.CalendarTagDao;
import fi.ohtu.mobilityprofile.domain.CalendarTag;

/**
 * This class creates suggestions based on data collected from the user's calendar.
 */
public class CalendarSuggestions implements SuggestionSource {
    private CalendarConnection calendar;
    private CalendarTagDao calendarTagDao;

    /**
     * Creates the CalendarSuggestions.
     *
     * @param calendar Interface for connecting to the calendar
     * @param calendarTagDao DAO for calendar tags
     */
    public CalendarSuggestions(CalendarConnection calendar, CalendarTagDao calendarTagDao) {
        this.calendar = calendar;
        this.calendarTagDao = calendarTagDao;
    }

    /**
     * Returns the most probable destinations from the calendar.
     *
     * @param startLocation Location where the user starts the journey
     * @return List of destinations from the calendar
     */
    @Override
    public List<Suggestion> getSuggestions(String startLocation) {
        List<Suggestion> suggestions = new ArrayList<>();

        // Loop through normal events first
        for (String eventLocation : calendar.getEventLocations()) {
            suggestions.add(createSuggestion(eventLocation));
        }

        // Then loop through all day event locations
        for (String eventLocation : calendar.getAllDayEventLocations()) {
            if (suggestions.size() >= 3) {
                // Only add all day event locations to the list if there are less than 3 events
                // added.
                break;
            }

            suggestions.add(createSuggestion(eventLocation));
        }

        return suggestions;
    }

    /**
     * Creates a suggestion object from the event location.
     * Changes the name of the location to a calendar tag if there is one with the given location
     * as a key.
     *
     * @param eventLocation Location of the event
     * @return Suggestion object
     */
    private Suggestion createSuggestion(String eventLocation) {
        // TODO: Fix calendar tags.
        // This functionality isn't working currently, as it makes mistakes too easily.
        /*
        CalendarTag calendarTag = calendarTagDao.findTheMostUsedTag(eventLocation);

        if (calendarTag != null) {
            eventLocation = calendarTag.getValue();
        }
        */

        return new Suggestion(eventLocation, SuggestionAccuracy.VERY_HIGH, CALENDAR_SUGGESTION);
    }
}

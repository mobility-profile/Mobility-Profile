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
     * Returns the most probable destination from the calendar.
     *
     * @return Destination from calendar
     */
    @Override
    public List<Suggestion> getSuggestions(String startLocation) {
        List<Suggestion> suggestions = new ArrayList<>();

        String eventLocation = calendar.getEventLocation();

        if (eventLocation != null) {
            CalendarTag calendarTag = calendarTagDao.findTheMostUsedTag(eventLocation);

            if (calendarTag != null) {
                eventLocation = calendarTag.getValue();
            }

            Suggestion suggestion = new Suggestion(eventLocation, SuggestionAccuracy.VERY_HIGH, CALENDAR_SUGGESTION);
            suggestions.add(suggestion);
        }

        return suggestions;
    }
}

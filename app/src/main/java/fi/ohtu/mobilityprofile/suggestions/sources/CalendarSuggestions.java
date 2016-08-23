package fi.ohtu.mobilityprofile.suggestions.sources;

import java.util.ArrayList;
import java.util.List;

import fi.ohtu.mobilityprofile.domain.StartLocation;
import fi.ohtu.mobilityprofile.util.CalendarConnection;
import fi.ohtu.mobilityprofile.suggestions.Suggestion;
import fi.ohtu.mobilityprofile.suggestions.SuggestionSource;

/**
 * This class creates suggestions based on data collected from the user's calendar.
 */
public class CalendarSuggestions implements SuggestionSource {
    private CalendarConnection calendar;

    /**
     * Creates the CalendarSuggestions.
     *
     * @param calendar Interface for connecting to the calendar
     */
    public CalendarSuggestions(CalendarConnection calendar) {
        this.calendar = calendar;
    }

    /**
     * Returns the most probable destinations from the calendar.
     *
     * @param startLocation Location where the user starts the journey
     * @return List of destinations from the calendar
     */
    @Override
    public List<Suggestion> getSuggestions(StartLocation startLocation) {
        List<Suggestion> suggestions = new ArrayList<>();

        // Loop through normal events first
        for (Suggestion suggestion : calendar.getEventLocations()) {
            suggestions.add(suggestion);
        }

        // Then loop through all day event locations
        for (Suggestion suggestion : calendar.getAllDayEventLocations()) {
            if (suggestions.size() >= 3) {
                // Only add all day event locations to the list if there are less than 3 events
                // added.
                break;
            }

            suggestions.add(suggestion);
        }

        return suggestions;
    }
}

package fi.ohtu.mobilityprofile.suggestions;

import java.util.List;

public interface SuggestionSource {
    int CALENDAR_SUGGESTION = 1;
    int VISIT_SUGGESTION = 2;
    int ROUTE_SUGGESTION = 3;
    int FAVORITE_SUGGESTION = 4;

    List<Suggestion> getSuggestions(String startLocation);
}

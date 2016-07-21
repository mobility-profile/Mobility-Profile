package fi.ohtu.mobilityprofile;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

import fi.ohtu.mobilityprofile.data.CalendarTagDao;
import fi.ohtu.mobilityprofile.data.FavouritePlaceDao;
import fi.ohtu.mobilityprofile.data.RouteSearchDao;
import fi.ohtu.mobilityprofile.data.UserLocationDao;
import fi.ohtu.mobilityprofile.data.VisitDao;
import fi.ohtu.mobilityprofile.suggestions.CalendarSuggestions;
import fi.ohtu.mobilityprofile.suggestions.DestinationLogic;
import fi.ohtu.mobilityprofile.suggestions.FavoriteSuggestions;
import fi.ohtu.mobilityprofile.suggestions.RouteSuggestions;
import fi.ohtu.mobilityprofile.suggestions.SuggestionSource;
import fi.ohtu.mobilityprofile.suggestions.locationHistory.VisitSuggestions;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifestTest.xml", constants = BuildConfig.class, sdk = 21)
public class DestinationLogicTest {

    private DestinationLogic mp;
    private VisitDao visitDao;
    private CalendarTagDao calendarTagDao;

    @Before
    public void setUp() throws Exception {
        CalendarTagDao calendarTagDao = mock(CalendarTagDao.class);
        visitDao = new VisitDao(mock(UserLocationDao.class));
        RouteSearchDao routeSearchDao = mock(RouteSearchDao.class);
        FavouritePlaceDao favouritePlaceDao = mock(FavouritePlaceDao.class);

        List<SuggestionSource> suggestionSources = new ArrayList<>();
        suggestionSources.add(new CalendarSuggestions(new CalendarConnection(Robolectric.setupActivity(MainActivityStub.class)), calendarTagDao));
        suggestionSources.add(new VisitSuggestions(visitDao));
        suggestionSources.add(new RouteSuggestions(routeSearchDao));
        suggestionSources.add(new FavoriteSuggestions(favouritePlaceDao));

        mp = new DestinationLogic(suggestionSources);

        when(calendarTagDao.findTheMostUsedTag(anyString())).thenReturn(null);
    }
}

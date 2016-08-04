package fi.ohtu.mobilityprofile.SuggestionTests;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

import fi.ohtu.mobilityprofile.BuildConfig;
import fi.ohtu.mobilityprofile.CalendarConnection;
import fi.ohtu.mobilityprofile.MainActivityStub;
import fi.ohtu.mobilityprofile.data.CalendarTagDao;
import fi.ohtu.mobilityprofile.data.FavouritePlaceDao;
import fi.ohtu.mobilityprofile.data.PlaceDao;
import fi.ohtu.mobilityprofile.data.RouteSearchDao;
import fi.ohtu.mobilityprofile.suggestions.sources.CalendarSuggestions;
import fi.ohtu.mobilityprofile.data.SignificantPlaceDao;
import fi.ohtu.mobilityprofile.data.VisitDao;
import fi.ohtu.mobilityprofile.suggestions.DestinationLogic;
import fi.ohtu.mobilityprofile.suggestions.sources.FavoriteSuggestions;
import fi.ohtu.mobilityprofile.suggestions.sources.RouteSuggestions;
import fi.ohtu.mobilityprofile.suggestions.SuggestionSource;
import fi.ohtu.mobilityprofile.suggestions.sources.VisitSuggestions;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifestTest.xml", constants = BuildConfig.class, sdk = 21)
public class DestinationLogicTest {

    private DestinationLogic mp;
    private PlaceDao placeDao;
    private CalendarTagDao calendarTagDao;

    @Before
    public void setUp() throws Exception {
        CalendarTagDao calendarTagDao = mock(CalendarTagDao.class);
        placeDao = new PlaceDao();
        RouteSearchDao routeSearchDao = mock(RouteSearchDao.class);
        FavouritePlaceDao favouritePlaceDao = mock(FavouritePlaceDao.class);

        List<SuggestionSource> suggestionSources = new ArrayList<>();
        suggestionSources.add(new CalendarSuggestions(new CalendarConnection(Robolectric.setupActivity(MainActivityStub.class)), calendarTagDao));
        suggestionSources.add(new VisitSuggestions(mock(SignificantPlaceDao.class), mock(VisitDao.class)));
        suggestionSources.add(new RouteSuggestions(routeSearchDao));
        suggestionSources.add(new FavoriteSuggestions(favouritePlaceDao));

        mp = new DestinationLogic(suggestionSources);

        when(calendarTagDao.findTheMostUsedTag(anyString())).thenReturn(null);
    }
}
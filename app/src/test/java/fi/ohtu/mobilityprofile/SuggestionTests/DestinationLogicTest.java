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
import fi.ohtu.mobilityprofile.suggestions.sources.CalendarSuggestions;
import fi.ohtu.mobilityprofile.suggestions.DestinationLogic;
import fi.ohtu.mobilityprofile.suggestions.sources.FavoriteSuggestions;
import fi.ohtu.mobilityprofile.suggestions.sources.RouteSuggestions;
import fi.ohtu.mobilityprofile.suggestions.SuggestionSource;
import fi.ohtu.mobilityprofile.suggestions.sources.VisitSuggestions;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifestTest.xml", constants = BuildConfig.class, sdk = 21)
public class DestinationLogicTest {

    private DestinationLogic mp;

    @Before
    public void setUp() throws Exception {
        CalendarTagDao calendarTagDao = mock(CalendarTagDao.class);

        List<SuggestionSource> suggestionSources = new ArrayList<>();
        suggestionSources.add(new CalendarSuggestions(new CalendarConnection(Robolectric.setupActivity(MainActivityStub.class))));
        suggestionSources.add(new VisitSuggestions());
        suggestionSources.add(new RouteSuggestions());
        suggestionSources.add(new FavoriteSuggestions());

        mp = new DestinationLogic(suggestionSources);

        when(CalendarTagDao.findTheMostUsedTag(anyString())).thenReturn(null);
    }
}
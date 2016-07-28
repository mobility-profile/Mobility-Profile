package fi.ohtu.mobilityprofile.SuggestionTests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.junit.Assert.*;

import fi.ohtu.mobilityprofile.BuildConfig;
import fi.ohtu.mobilityprofile.MainActivityStub;
import fi.ohtu.mobilityprofile.data.RouteSearchDao;
import fi.ohtu.mobilityprofile.domain.RouteSearch;
import fi.ohtu.mobilityprofile.suggestions.RouteSuggestions;
import fi.ohtu.mobilityprofile.suggestions.Suggestion;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifestTest.xml", constants = BuildConfig.class, sdk = 21)
public class RouteSuggestionsTest {
    private RouteSearchDao routeSearchDao;
    private RouteSuggestions routeSuggestions;
    private final int HOUR = 3600 * 1000;

    @Before
    public void setUp() {
        this.routeSearchDao = new RouteSearchDao();
        this.routeSuggestions = new RouteSuggestions(this.routeSearchDao);
        Robolectric.setupActivity(MainActivityStub.class);
    }

    @Test
    public void testGetCorrectSuggestion() {
        routeSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis(), "Lauttasaari", "Ruoholahti"));
        routeSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis() + 3 * HOUR, "Lauttasaari",  "Kamppi"));
        routeSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis(), "Pitäjänmäki",  "Sörnäinen"));

        List<Suggestion> suggestions = routeSuggestions.getSuggestions("Lauttasaari");
        assertEquals(1, suggestions.size());
        assertEquals("Ruoholahti", suggestions.get(0).getDestination());
    }

    @Test
    public void testGetZeroSuggestions() {
        routeSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis(), "Lauttasaari", "Ruoholahti"));
        routeSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis(), "Lauttasaari",  "Kamppi"));
        routeSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis(), "Pitäjänmäki",  "Sörnäinen"));
        routeSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis() + 3 * HOUR, "Ruoholahti", "Lauttasaari"));

        List<Suggestion> suggestions = routeSuggestions.getSuggestions("Ruoholahti");
        assertEquals(0, suggestions.size());
    }

    @Test
    public void testGetMultipleSuggestions() {
        routeSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis() + HOUR, "Lauttasaari", "Ruoholahti"));
        routeSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis() - HOUR, "Lauttasaari",  "Kamppi"));
        routeSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis(), "Pitäjänmäki",  "Sörnäinen"));

        List<Suggestion> suggestions = routeSuggestions.getSuggestions("Lauttasaari");
        assertEquals(2, suggestions.size());
    }
}

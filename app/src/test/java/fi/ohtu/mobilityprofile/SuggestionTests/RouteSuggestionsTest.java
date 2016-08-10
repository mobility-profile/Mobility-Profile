package fi.ohtu.mobilityprofile.SuggestionTests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

import fi.ohtu.mobilityprofile.BuildConfig;
import fi.ohtu.mobilityprofile.MainActivityStub;
import fi.ohtu.mobilityprofile.data.RouteSearchDao;
import fi.ohtu.mobilityprofile.domain.GpsPoint;
import fi.ohtu.mobilityprofile.domain.RouteSearch;
import fi.ohtu.mobilityprofile.suggestions.sources.RouteSuggestions;
import fi.ohtu.mobilityprofile.suggestions.Suggestion;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifestTest.xml", constants = BuildConfig.class, sdk = 21)
public class RouteSuggestionsTest {
    private RouteSuggestions routeSuggestions;
    private final int HOUR = 3600 * 1000;

    @Before
    public void setUp() {
        this.routeSuggestions = new RouteSuggestions();
        Robolectric.setupActivity(MainActivityStub.class);
    }

    @Test
    public void testGetCorrectSuggestion() {
        RouteSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis(), "Lauttasaari", "Ruoholahti"));
        RouteSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis() + 3 * HOUR, "Lauttasaari",  "Kamppi"));
        RouteSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis(), "Pitäjänmäki",  "Sörnäinen"));

        List<Suggestion> suggestions = routeSuggestions.getSuggestions(new GpsPoint(0, 0, 0f, 0f));
        assertEquals(1, suggestions.size());
        assertEquals("Ruoholahti", suggestions.get(0).getDestination());
    }

    @Test
    public void testGetZeroSuggestions() {
        RouteSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis(), "Lauttasaari", "Ruoholahti"));
        RouteSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis(), "Lauttasaari",  "Kamppi"));
        RouteSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis(), "Pitäjänmäki",  "Sörnäinen"));
        RouteSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis() + 3 * HOUR, "Ruoholahti", "Lauttasaari"));

        List<Suggestion> suggestions = routeSuggestions.getSuggestions(new GpsPoint(0, 0, 0f, 0f));
        assertEquals(0, suggestions.size());
    }

    @Test
    public void testGetMultipleSuggestions() {
        RouteSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis() + HOUR, "Lauttasaari", "Ruoholahti"));
        RouteSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis() - HOUR, "Lauttasaari",  "Kamppi"));
        RouteSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis(), "Pitäjänmäki",  "Sörnäinen"));

        List<Suggestion> suggestions = routeSuggestions.getSuggestions(new GpsPoint(0, 0, 0f, 0f));
        assertEquals(2, suggestions.size());

        List<String> destinations = new ArrayList<>();
        destinations.add(suggestions.get(0).getDestination());
        destinations.add(suggestions.get(1).getDestination());
        assertTrue(destinations.contains("Ruoholahti"));
        assertTrue(destinations.contains("Kamppi"));
    }
}

package fi.ohtu.mobilityprofile.SuggestionTests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

import fi.ohtu.mobilityprofile.BuildConfig;
import fi.ohtu.mobilityprofile.data.PlaceDao;
import fi.ohtu.mobilityprofile.data.RouteSearchDao;
import fi.ohtu.mobilityprofile.domain.Coordinate;
import fi.ohtu.mobilityprofile.domain.Place;
import fi.ohtu.mobilityprofile.domain.RouteSearch;
import fi.ohtu.mobilityprofile.domain.StartLocation;
import fi.ohtu.mobilityprofile.suggestions.Suggestion;
import fi.ohtu.mobilityprofile.suggestions.sources.FavoriteSuggestions;
import fi.ohtu.mobilityprofile.suggestions.sources.InterCitySuggestions;
import fi.ohtu.mobilityprofile.data.CalendarTagDao;
import fi.ohtu.mobilityprofile.suggestions.DestinationLogic;
import fi.ohtu.mobilityprofile.suggestions.sources.RouteSuggestions;
import fi.ohtu.mobilityprofile.suggestions.SuggestionSource;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifestTest.xml", constants = BuildConfig.class, sdk = 21)
public class DestinationLogicTest {

    private DestinationLogic mp;
    private static RouteSearchDao routeSearchDao;
    private static PlaceDao placeDao;

    private Coordinate laut;

    @Before
    public void setUp() throws Exception {

        CalendarTagDao calendarTagDao = mock(CalendarTagDao.class);

        List<SuggestionSource> suggestionSources = new ArrayList<>();
        //suggestionSources.add(new CalendarSuggestions(new CalendarConnection(Robolectric.setupActivity(MainActivityStub.class))));
        //suggestionSources.add(new VisitSuggestions());
        suggestionSources.add(new FavoriteSuggestions());
        suggestionSources.add(new RouteSuggestions());
        //suggestionSources.add(new FavoriteSuggestions());

        mp = new DestinationLogic(suggestionSources, new InterCitySuggestions());

        // when(CalendarTagDao.findTheMostUsedTag(anyString())).thenReturn(null);
    }

//    @Test
//    public void testGeoJSONSuggestions() {
//        routeSearchDao = new RouteSearchDao();
//        routeSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis(), "Kumpulan kampus", "Töölö",
//                new Coordinate(new Float(60.203978), new Float(24.965546)),
//                new Coordinate(new Float(60.174892), new Float(24.921637))));
//
//        String suggestionString = mp.getMostLikelyDestinations(new GpsPoint(System.currentTimeMillis(), new Float(60.203978), new Float(24.965546)));
//        JSONArray suggestions = null;
//        String result = "";
//
//        try {
//            suggestions = new JSONArray(suggestionString);
//            result = suggestions.getJSONObject(0).getJSONObject("properties").get("destination").toString();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        assertEquals("Töölö", result);
//    }

    @Test
    public void suggestionsAreSortedBasedOnAccuracy() {
        insertDataToDatabase();

        mp.getListOfIntraCitySuggestions(new StartLocation(343423, 50, laut.getLatitude(), laut.getLongitude()));
        List<Suggestion> suggestions = mp.getLatestSuggestions();

        assertEquals("Sörnäinen", suggestions.get(0).getDestination());
    }

    private void insertDataToDatabase() {
        Coordinate sorn = new Coordinate(new Float(60.186422), new Float(24.968971));
        laut = new Coordinate(new Float(60.157330), new Float(24.877253));
        Coordinate haka = new Coordinate(new Float(60.17885), new Float(24.95006));
        Coordinate pita = new Coordinate(new Float(60.222980), new Float(24.862062));
        routeSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis(), "Lauttasaari",  "Sörnäinen", laut, sorn));
        routeSearchDao.insertRouteSearch(new RouteSearch(23232, "Lauttasaari",  "Sörnäinen", laut, sorn));
        routeSearchDao.insertRouteSearch(new RouteSearch(90000, "Hakaniemi",  "Pitäjänmäki", haka, pita));
        routeSearchDao.insertRouteSearch(new RouteSearch(10000, "Pitäjänmäki",  "Sörnäinen", pita, sorn));

        Place kamppi = new Place("Kamppi", "Kamppi", new Coordinate(new Float(60.167580), new Float(24.930226)));
        Place kumpula = new Place("Kumpula", "Kumpula", new Coordinate(new Float(60.209108), new Float(24.964735)));
        kamppi.setFavourite(true);
        kumpula.setFavourite(true);
        placeDao.insertPlace(kamppi);
        placeDao.insertPlace(kumpula);
    }
}
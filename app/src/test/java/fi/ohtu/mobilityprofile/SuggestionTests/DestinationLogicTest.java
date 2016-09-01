package fi.ohtu.mobilityprofile.SuggestionTests;

import android.location.Address;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
import fi.ohtu.mobilityprofile.suggestions.DestinationLogic;
import fi.ohtu.mobilityprofile.suggestions.sources.RouteSuggestions;
import fi.ohtu.mobilityprofile.suggestions.SuggestionSource;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifestTest.xml", constants = BuildConfig.class, sdk = 21)
public class DestinationLogicTest {

    private DestinationLogic mp;
    private RouteSearchDao routeSearchDao;
    private PlaceDao placeDao;
    private final long DAY = 2073600000;

    private Coordinate lautCoord;

    @Before
    public void setUp() throws Exception {
        placeDao = new PlaceDao();
        routeSearchDao = new RouteSearchDao();
        List<SuggestionSource> suggestionSources = new ArrayList<>();
        //suggestionSources.add(new CalendarSuggestions(new CalendarConnection(Robolectric.setupActivity(MainActivityStub.class))));
        //suggestionSources.add(new VisitSuggestions());
        suggestionSources.add(new FavoriteSuggestions());
        suggestionSources.add(new RouteSuggestions());

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

        mp.getListOfIntraCitySuggestions(new StartLocation(System.currentTimeMillis(), 50, lautCoord.getLatitude(), lautCoord.getLongitude()));
        List<Suggestion> suggestions = mp.getLatestSuggestions();
        assertNotNull(suggestions);
        assertEquals("Sörnäinen", suggestions.get(0).getDestination().getName());
    }

    private void insertDataToDatabase() {
        lautCoord = new Coordinate(new Float(60.157330), new Float(24.877253));
        Place kumpula = new Place("Kumpula", new Address(Locale.getDefault()));
        Place sornainen = new Place("Sörnäinen", new Address(Locale.getDefault()));
        Place lauttasaari = new Place("Lauttasaari", new Address(Locale.getDefault()));
        Place hakaniemi = new Place("Hakaniemi", new Address(Locale.getDefault()));
        kumpula.setCoordinate(new Coordinate(new Float(60.209108), new Float(24.964735)));
        sornainen.setCoordinate(new Coordinate(new Float(60.186422), new Float(24.968971)));
        lauttasaari.setCoordinate(lautCoord);
        hakaniemi.setCoordinate(new Coordinate(new Float(60.17885), new Float(24.95006)));

        hakaniemi.setFavourite(true);
        kumpula.setFavourite(true);
        placeDao.insertPlace(hakaniemi);
        placeDao.insertPlace(kumpula);
        placeDao.insertPlace(sornainen);
        placeDao.insertPlace(lauttasaari);

        PlaceDao.insertPlace(kumpula);
        routeSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis() - DAY, 0, sornainen, lauttasaari));
        routeSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis() - 2 * DAY, 0, lauttasaari, sornainen));
        routeSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis() - 3 * DAY, 0, hakaniemi, lauttasaari));
        routeSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis() - 4 * DAY, 0, kumpula, sornainen));
    }
}
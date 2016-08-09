package fi.ohtu.mobilityprofile.SuggestionTests;

import android.util.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONStringer;
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
import fi.ohtu.mobilityprofile.data.RouteSearchDao;
import fi.ohtu.mobilityprofile.domain.Coordinate;
import fi.ohtu.mobilityprofile.domain.GPSPoint;
import fi.ohtu.mobilityprofile.domain.RouteSearch;
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

    @Before
    public void setUp() throws Exception {
        CalendarTagDao calendarTagDao = mock(CalendarTagDao.class);

        List<SuggestionSource> suggestionSources = new ArrayList<>();
        //suggestionSources.add(new CalendarSuggestions(new CalendarConnection(Robolectric.setupActivity(MainActivityStub.class))));
        //suggestionSources.add(new VisitSuggestions());
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
//        String suggestionString = mp.getMostLikelyDestinations(new GPSPoint(System.currentTimeMillis(), new Float(60.203978), new Float(24.965546)));
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
}

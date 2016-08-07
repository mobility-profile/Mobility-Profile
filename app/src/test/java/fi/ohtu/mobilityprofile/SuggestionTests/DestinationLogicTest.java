package fi.ohtu.mobilityprofile.SuggestionTests;

import com.cocoahero.android.geojson.Feature;
import com.cocoahero.android.geojson.GeoJSON;
import com.cocoahero.android.geojson.GeoJSONObject;
import com.google.api.client.json.Json;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
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
import fi.ohtu.mobilityprofile.suggestions.Suggestion;
import fi.ohtu.mobilityprofile.util.CalendarConnection;
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
    private static RouteSearchDao routeSearchDao;

    @Before
    public void setUp() throws Exception {
        CalendarTagDao calendarTagDao = mock(CalendarTagDao.class);

        List<SuggestionSource> suggestionSources = new ArrayList<>();
        //suggestionSources.add(new CalendarSuggestions(new CalendarConnection(Robolectric.setupActivity(MainActivityStub.class))));
        //suggestionSources.add(new VisitSuggestions());
        suggestionSources.add(new RouteSuggestions());
        //suggestionSources.add(new FavoriteSuggestions());

        mp = new DestinationLogic(suggestionSources);

       // when(CalendarTagDao.findTheMostUsedTag(anyString())).thenReturn(null);
    }

    @Test
    public void testGeoJSONSuggestions() {
        routeSearchDao = new RouteSearchDao();
        routeSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis(), "Kumpulan kampus", "Töölö",
                new Coordinate(new Float(60.203978), new Float(24.965546)),
                new Coordinate(new Float(60.174892), new Float(24.921637))));
        JSONArray suggestions = mp.getListOfMostLikelyDestinationsJSON(new GPSPoint(System.currentTimeMillis(), new Float(60.203978), new Float(24.965546)));

        GeoJSONObject geo = null;
        if (suggestions.length() > 0) {
            try {
                geo = GeoJSON.parse(suggestions.getJSONObject(0));
            } catch (Exception e) {
            }
        }
        System.out.println(geo);
    }
}
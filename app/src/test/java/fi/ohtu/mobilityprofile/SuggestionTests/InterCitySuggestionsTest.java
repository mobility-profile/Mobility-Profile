package fi.ohtu.mobilityprofile.SuggestionTests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import fi.ohtu.mobilityprofile.BuildConfig;
import fi.ohtu.mobilityprofile.MainActivityStub;
import fi.ohtu.mobilityprofile.data.InterCitySearchDao;
import fi.ohtu.mobilityprofile.domain.InterCitySearch;
import fi.ohtu.mobilityprofile.domain.StartLocation;
import fi.ohtu.mobilityprofile.suggestions.Suggestion;
import fi.ohtu.mobilityprofile.suggestions.sources.InterCitySuggestions;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifestTest.xml", constants = BuildConfig.class, sdk = 21)
public class InterCitySuggestionsTest {

    private InterCitySuggestions interCitySuggestions;

    @Before
    public void setUp() {
        this.interCitySuggestions = new InterCitySuggestions();
        Robolectric.setupActivity(MainActivityStub.class);
        createInterCitySearches();
    }

    @Test
    public void getMaxFiveSuggestions() {
        List<Suggestion> results = interCitySuggestions.getSuggestions(new StartLocation(767565, 50, new Float(60.209108), new Float(24.964735)));
        assertEquals(5, results.size());

    }

    private void createInterCitySearches() {
        InterCitySearchDao dao = new InterCitySearchDao();
        dao.insertInterCitySearch(new InterCitySearch("Helsinki", "Tampere", 122324));
        dao.insertInterCitySearch(new InterCitySearch("Tampere", "Turku", 232411));
        dao.insertInterCitySearch(new InterCitySearch("Helsinki", "Tampere", 532434));
        dao.insertInterCitySearch(new InterCitySearch("Helsinki", "Turku", 772411));
        dao.insertInterCitySearch(new InterCitySearch("Turku", "Tampere", 885454));
        dao.insertInterCitySearch(new InterCitySearch("Tampere", "Helsinki", 987541));
    }
}


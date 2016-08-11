package fi.ohtu.mobilityprofile.SuggestionTests;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import fi.ohtu.mobilityprofile.BuildConfig;
import fi.ohtu.mobilityprofile.MainActivityStub;
import fi.ohtu.mobilityprofile.data.FavouritePlaceDao;
import fi.ohtu.mobilityprofile.domain.FavouritePlace;
import fi.ohtu.mobilityprofile.domain.GpsPoint;
import fi.ohtu.mobilityprofile.suggestions.sources.FavoriteSuggestions;
import fi.ohtu.mobilityprofile.suggestions.Suggestion;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifestTest.xml", constants = BuildConfig.class, sdk = 21)
public class FavoriteSuggestionsTest {
    private FavoriteSuggestions favoriteSuggestions;

    @Before
    public void setUp() {
        this.favoriteSuggestions = new FavoriteSuggestions();
        Robolectric.setupActivity(MainActivityStub.class);
    }

    @Test
    public void testGetSuggestionsIsLimitedToThree() {
        FavouritePlaceDao.insertFavouritePlace(new FavouritePlace("Koulu", "Kumpulan kampus"));
        FavouritePlaceDao.insertFavouritePlace(new FavouritePlace("Sali", "Urheilijankatu"));
        FavouritePlaceDao.insertFavouritePlace(new FavouritePlace("Repe", "Sorsankatu"));
        FavouritePlaceDao.insertFavouritePlace(new FavouritePlace("Kauppa", "Kauppakatu"));

        List<Suggestion> suggestions = favoriteSuggestions.getSuggestions(new GpsPoint(0, 0, 0f, 0f));
        assertEquals(3, suggestions.size());
    }

    @Test
    public void testGetSuggestionsReturnsListWhereFirstSuggestionHasBiggestCounter() {
        FavouritePlace koulu = new FavouritePlace("Koulu", "Kumpulan kampus");
        FavouritePlace kauppa = new FavouritePlace("Kauppa", "Kauppakatu");
        koulu.increaseCounter();
        koulu.increaseCounter();
        kauppa.increaseCounter();
        FavouritePlaceDao.insertFavouritePlace(kauppa);
        FavouritePlaceDao.insertFavouritePlace(koulu);
        FavouritePlaceDao.insertFavouritePlace(new FavouritePlace("Sali", "Urheilijankatu"));
        FavouritePlaceDao.insertFavouritePlace(new FavouritePlace("Repe", "Sorsankatu"));

        List<Suggestion> suggestions = favoriteSuggestions.getSuggestions(new GpsPoint(0, 0, 0f, 0f));

        assertEquals("Kumpulan kampus", suggestions.get(0).getDestination());
    }

    @Test
    public void testGetZeroSuggestions() {
        List<Suggestion> suggestions = favoriteSuggestions.getSuggestions(new GpsPoint(0, 0, 0f, 0f));

        assertEquals(0, suggestions.size());
    }
}

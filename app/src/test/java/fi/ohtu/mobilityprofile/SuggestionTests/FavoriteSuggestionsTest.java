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
import fi.ohtu.mobilityprofile.suggestions.FavoriteSuggestions;
import fi.ohtu.mobilityprofile.suggestions.Suggestion;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifestTest.xml", constants = BuildConfig.class, sdk = 21)
public class FavoriteSuggestionsTest {
    private FavoriteSuggestions favoriteSuggestions;
    private FavouritePlaceDao favouritePlaceDao;

    @Before
    public void setUp() {
        this.favouritePlaceDao = new FavouritePlaceDao();
        this.favoriteSuggestions = new FavoriteSuggestions(this.favouritePlaceDao);
        Robolectric.setupActivity(MainActivityStub.class);
    }

    @Test
    public void testGetSuggestionsIsLimitedToThree() {
        favouritePlaceDao.insertFavouritePlace(new FavouritePlace("Koulu", "Kumpulan kampus"));
        favouritePlaceDao.insertFavouritePlace(new FavouritePlace("Sali", "Urheilijankatu"));
        favouritePlaceDao.insertFavouritePlace(new FavouritePlace("Repe", "Sorsankatu"));
        favouritePlaceDao.insertFavouritePlace(new FavouritePlace("Kauppa", "Kauppakatu"));

        List<Suggestion> suggestions = favoriteSuggestions.getSuggestions("Rautatientori");
        assertEquals(3, suggestions.size());
    }

    @Test
    public void testGetSuggestionsReturnsListWhereFirstSuggestionHasBiggestCounter() {
        FavouritePlace koulu = new FavouritePlace("Koulu", "Kumpulan kampus");
        FavouritePlace kauppa = new FavouritePlace("Kauppa", "Kauppakatu");
        koulu.increaseCounter();
        koulu.increaseCounter();
        kauppa.increaseCounter();
        favouritePlaceDao.insertFavouritePlace(kauppa);
        favouritePlaceDao.insertFavouritePlace(koulu);
        favouritePlaceDao.insertFavouritePlace(new FavouritePlace("Sali", "Urheilijankatu"));
        favouritePlaceDao.insertFavouritePlace(new FavouritePlace("Repe", "Sorsankatu"));

        List<Suggestion> suggestions = favoriteSuggestions.getSuggestions("Rautatientori");

        assertEquals("Kumpulan kampus", suggestions.get(0).getDestination());
    }

    @Test
    public void testGetZeroSuggestions() {
        List<Suggestion> suggestions = favoriteSuggestions.getSuggestions("Rautatientori");

        assertEquals(0, suggestions.size());
    }
}

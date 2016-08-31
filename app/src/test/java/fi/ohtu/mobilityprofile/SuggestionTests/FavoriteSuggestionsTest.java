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
import fi.ohtu.mobilityprofile.data.PlaceDao;
import fi.ohtu.mobilityprofile.domain.Coordinate;
import fi.ohtu.mobilityprofile.domain.Place;
import fi.ohtu.mobilityprofile.domain.StartLocation;
import fi.ohtu.mobilityprofile.suggestions.sources.FavoriteSuggestions;
import fi.ohtu.mobilityprofile.suggestions.Suggestion;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifestTest.xml", constants = BuildConfig.class, sdk = 21)
public class FavoriteSuggestionsTest {
    private FavoriteSuggestions favoriteSuggestions;

    private Place kumpula;
    private Place hakaniemi;
    private Place lauttasaari;

    @Before
    public void setUp() {
        this.favoriteSuggestions = new FavoriteSuggestions();
        Robolectric.setupActivity(MainActivityStub.class);
        createPlaces();
    }

    @Test
    public void getSuggestionsWhenAllPlacesAreFavourited() {
        kumpula.setFavourite(true);
        lauttasaari.setFavourite(true);
        hakaniemi.setFavourite(true);
        List<Suggestion> results = favoriteSuggestions.getSuggestions(new StartLocation(123124, 50, new Float(60.17885), new Float(24.95006)));
        assertEquals(3, results.size());
    }

    @Test
    public void getOneSuggestionWhenOnlyOnePlaceIsFavourited() {
        kumpula.setFavourite(true);
        List<Suggestion> results = favoriteSuggestions.getSuggestions(new StartLocation(123124, 50, new Float(60.17885), new Float(24.95006)));
        assertEquals(1, results.size());
        assertEquals(kumpula.getCoordinate(), results.get(0).getCoordinate());
    }

    private void createPlaces() {
        kumpula = new Place("Kumpula", "Kumpula", new Coordinate(new Float(60.209108), new Float(24.964735)));
        lauttasaari = new Place("Lauttasaari", "Lauttasaari", new Coordinate(new Float(60.157330), new Float(24.877253)));
        hakaniemi = new Place("Hakaniemi", "Hakaniemi", new Coordinate(new Float(60.17885), new Float(24.95006)));
        PlaceDao.insertPlace(kumpula);
        PlaceDao.insertPlace(lauttasaari);
        PlaceDao.insertPlace(hakaniemi);
    }
}

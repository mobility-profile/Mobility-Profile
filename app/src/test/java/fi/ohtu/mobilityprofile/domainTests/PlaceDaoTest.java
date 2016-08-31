package fi.ohtu.mobilityprofile.domainTests;

import android.location.Address;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Locale;

import fi.ohtu.mobilityprofile.BuildConfig;
import fi.ohtu.mobilityprofile.MainActivityStub;
import fi.ohtu.mobilityprofile.data.PlaceDao;
import fi.ohtu.mobilityprofile.domain.Coordinate;
import fi.ohtu.mobilityprofile.domain.Place;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifestTest.xml", constants = BuildConfig.class, sdk = 21)
public class PlaceDaoTest {
    private static PlaceDao placeDao;

    private Place kumpula;
    private Place sornainen;
    private Place lauttasaari;
    private Place hakaniemi;

    @Before
    public void setUp() {
        this.placeDao = new PlaceDao();
        createPlaces();
        Robolectric.setupActivity(MainActivityStub.class);
    }

    @Test
    public void testGetPlaceClosestTo() {
        placeDao.insertPlace(kumpula);
        placeDao.insertPlace(hakaniemi);
        placeDao.insertPlace(lauttasaari);

        Place result = placeDao.getPlaceClosestTo(sornainen.getCoordinate());
        assertEquals("Hakaniemi", result.getName());

    }

    @Test
    public void testInsert() {
        placeDao.insertPlace(kumpula);
        assertEquals("Kumpula", placeDao.getAll().get(0).getName());
    }

    @Test
    public void coordinateIsSavedIntoDatabase() {
        placeDao.insertPlace(kumpula);
        Place place = placeDao.getPlaceByName("Kumpula");
        assertTrue(place.getCoordinate() != null);
    }

    @Test
    public void testGetPlaceByName() {
        placeDao.insertPlace(kumpula);
        Place place = placeDao.getPlaceByName("Kumpula");
        assertTrue(place != null);
        assertEquals("Kumpula", place.getName());
    }

    @Test
    public void testInsertMultipleAndFindByName() {
        placeDao.insertPlace(kumpula);
        placeDao.insertPlace(hakaniemi);
        placeDao.insertPlace(lauttasaari);

        Place place = placeDao.getPlaceByName("Hakaniemi");
        assertTrue(place != null);
        assertEquals("Hakaniemi", place.getName());
    }

    @Test
    public void testFindNothing() {
        placeDao.insertPlace(kumpula);
        placeDao.insertPlace(hakaniemi);
        placeDao.insertPlace(lauttasaari);

        Place place = placeDao.getPlaceByName("Kamppi");
        assertTrue(place == null);
    }

    @Test
    public void testGetFavouritePlaces() {
        kumpula.setFavourite(true);
        placeDao.insertPlace(kumpula);

       assertEquals("Kumpula", placeDao.getFavouritePlaces().get(0).getName());
    }

    @Test
    public void getMultipleFavouritePlaces() {
        kumpula.setFavourite(true);
        hakaniemi.setFavourite(true);
        placeDao.insertPlace(kumpula);
        placeDao.insertPlace(hakaniemi);
        placeDao.insertPlace(lauttasaari);

        assertEquals(2, placeDao.getFavouritePlaces().size());
    }

    @Test
    public void testGetFavouritePlacesInJson() throws JSONException {
        kumpula.setFavourite(true);
        placeDao.insertPlace(kumpula);

        String json = placeDao.getFavouritePlacesInJson();
        JSONArray favourites = new JSONArray(json);

        assertTrue(favourites != null);
        assertEquals("Kumpula", favourites.getJSONObject(0).getJSONObject("properties").getString("label"));
    }

    @Test
    public void testDeleteAllData() {
        placeDao.insertPlace(kumpula);
        placeDao.insertPlace(hakaniemi);
        placeDao.insertPlace(lauttasaari);

        assertEquals(3, placeDao.getAll().size());
        placeDao.deleteAllData();
        assertTrue(placeDao.getAll().isEmpty());
    }

    private void createPlaces() {
        kumpula = new Place("Kumpula", new Address(Locale.getDefault()));
        sornainen = new Place("Sörnäinen", new Address(Locale.getDefault()));
        lauttasaari = new Place("Lauttasaari", new Address(Locale.getDefault()));
        hakaniemi = new Place("Hakaniemi", new Address(Locale.getDefault()));
        kumpula.setCoordinate(new Coordinate(new Float(60.209108), new Float(24.964735)));
        sornainen.setCoordinate(new Coordinate(new Float(60.186422), new Float(24.968971)));
        lauttasaari.setCoordinate(new Coordinate(new Float(60.157330), new Float(24.877253)));
        hakaniemi.setCoordinate(new Coordinate(new Float(60.17885), new Float(24.95006)));
    }
}

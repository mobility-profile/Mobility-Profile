package fi.ohtu.mobilityprofile.domainTests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

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

    @Before
    public void setUp() {
        this.placeDao = new PlaceDao();
        Robolectric.setupActivity(MainActivityStub.class);
    }

    @Test
    public void coordinateIsSavedIntoDatabase() {
        placeDao.insertPlace(new Place("Koulu", "Kumpula", new Coordinate(new Float(1), new Float(2))));
        Place place = placeDao.getPlaceByAddress("Kumpula");
        assertTrue(place.getCoordinate() != null);
    }

    @Test
    public void testInsertandFindByAddress() {
        placeDao.insertPlace(new Place("Koulu", "Kumpula", new Coordinate(new Float(1), new Float(2))));
        Place place = placeDao.getPlaceByAddress("Kumpula");
        assertTrue(place != null);
        assertEquals("Kumpula", place.getAddress());
    }

    @Test
    public void testInsertMultipleAndFindByLocation() {
        placeDao.insertPlace(new Place("Koulu", "Kumpula", new Coordinate(new Float(1), new Float(2))));
        placeDao.insertPlace(new Place("Toimisto", "Töölö", new Coordinate(new Float(1), new Float(2))));
        placeDao.insertPlace(new Place("Kauppa", "Kauppakatu", new Coordinate(new Float(1), new Float(2))));
        Place place = placeDao.getPlaceByAddress("Töölö");
        assertTrue(place != null);
        assertEquals("Töölö", place.getAddress());
    }

    @Test
    public void testGetSignificantPlaceByName() {
        placeDao.insertPlace(new Place("Koulu", "Kumpula", new Coordinate(new Float(1), new Float(2))));
        placeDao.insertPlace(new Place("Toimisto", "Töölö", new Coordinate(new Float(1), new Float(2))));
        placeDao.insertPlace(new Place("Kauppa", "Kauppakatu", new Coordinate(new Float(1), new Float(2))));
        Place place = placeDao.getPlaceByName("Toimisto");
        assertTrue(place != null);
        assertEquals("Töölö", place.getAddress());
    }

    @Test
    public void testFindNothing() {
        placeDao.insertPlace(new Place("Koulu", "Kumpula", new Coordinate(new Float(1), new Float(2))));
        placeDao.insertPlace(new Place("Toimisto", "Töölö", new Coordinate(new Float(1), new Float(2))));
        placeDao.insertPlace(new Place("Kauppa", "Kauppakatu", new Coordinate(new Float(1), new Float(2))));

        Place place = placeDao.getPlaceByAddress("Kamppi");
        assertTrue(place == null);
    }

    @Test
    public void deleteAll() {
        placeDao.insertPlace(new Place("Koulu", "Kumpula", new Coordinate(new Float(1), new Float(2))));
        placeDao.insertPlace(new Place("Toimisto", "Töölö", new Coordinate(new Float(1), new Float(2))));
        placeDao.insertPlace(new Place("Kauppa", "Kauppakatu", new Coordinate(new Float(1), new Float(2))));
        assertTrue(placeDao.getAll().size() == 3);
        placeDao.deleteAllData();
        assertTrue(placeDao.getAll().isEmpty());
    }

    @Test
    public void deleteSignificantPlaceByAddress() {
        placeDao.insertPlace(new Place("Koulu", "Kumpula", new Coordinate(new Float(1), new Float(2))));
        placeDao.insertPlace(new Place("Toimisto", "Töölö", new Coordinate(new Float(1), new Float(2))));
        assertTrue(placeDao.getAll().size() == 2);
        placeDao.deletePlaceByAddress("Töölö");
        assertTrue(placeDao.getAll().size() == 1);
        assertEquals("Kumpula", placeDao.getAll().get(0).getAddress());
    }
}

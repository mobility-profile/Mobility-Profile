package fi.ohtu.mobilityprofile.domainTests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import fi.ohtu.mobilityprofile.BuildConfig;
import fi.ohtu.mobilityprofile.MainActivityStub;
import fi.ohtu.mobilityprofile.data.SignificantPlaceDao;
import fi.ohtu.mobilityprofile.domain.Coordinate;
import fi.ohtu.mobilityprofile.domain.SignificantPlace;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifestTest.xml", constants = BuildConfig.class, sdk = 21)
public class SignificantPlaceDaoTest {
    private static SignificantPlaceDao significantPlaceDao;

    @Before
    public void setUp() {
        this.significantPlaceDao = new SignificantPlaceDao();
        Robolectric.setupActivity(MainActivityStub.class);
    }

    @Test
    public void testInsertandFindByAddress() {
        significantPlaceDao.insertSignificantPlace(new SignificantPlace("Koulu", "Kumpula", new Coordinate(new Float(1), new Float(2))));
        SignificantPlace place = significantPlaceDao.getSignificantPlaceByAddress("Kumpula");
        assertTrue(place != null);
        assertEquals("Kumpula", place.getAddress());
    }

    @Test
    public void testInsertMultipleAndFindByLocation() {
        significantPlaceDao.insertSignificantPlace(new SignificantPlace("Koulu", "Kumpula", new Coordinate(new Float(1), new Float(2))));
        significantPlaceDao.insertSignificantPlace(new SignificantPlace("Toimisto", "Töölö", new Coordinate(new Float(1), new Float(2))));
        significantPlaceDao.insertSignificantPlace(new SignificantPlace("Kauppa", "Kauppakatu", new Coordinate(new Float(1), new Float(2))));
        SignificantPlace place = significantPlaceDao.getSignificantPlaceByAddress("Töölö");
        assertTrue(place != null);
        assertEquals("Töölö", place.getAddress());
    }

    @Test
    public void testGetSignificantPlaceByName() {
        significantPlaceDao.insertSignificantPlace(new SignificantPlace("Koulu", "Kumpula", new Coordinate(new Float(1), new Float(2))));
        significantPlaceDao.insertSignificantPlace(new SignificantPlace("Toimisto", "Töölö", new Coordinate(new Float(1), new Float(2))));
        significantPlaceDao.insertSignificantPlace(new SignificantPlace("Kauppa", "Kauppakatu", new Coordinate(new Float(1), new Float(2))));
        SignificantPlace place = significantPlaceDao.getSignificantPlaceByName("Toimisto");
        assertTrue(place != null);
        assertEquals("Töölö", place.getAddress());
    }

    @Test
    public void testFindNothing() {
        significantPlaceDao.insertSignificantPlace(new SignificantPlace("Koulu", "Kumpula", new Coordinate(new Float(1), new Float(2))));
        significantPlaceDao.insertSignificantPlace(new SignificantPlace("Toimisto", "Töölö", new Coordinate(new Float(1), new Float(2))));
        significantPlaceDao.insertSignificantPlace(new SignificantPlace("Kauppa", "Kauppakatu", new Coordinate(new Float(1), new Float(2))));

        SignificantPlace place = significantPlaceDao.getSignificantPlaceByAddress("Kamppi");
        assertTrue(place == null);
    }

    @Test
    public void deleteAll() {
        significantPlaceDao.insertSignificantPlace(new SignificantPlace("Koulu", "Kumpula", new Coordinate(new Float(1), new Float(2))));
        significantPlaceDao.insertSignificantPlace(new SignificantPlace("Toimisto", "Töölö", new Coordinate(new Float(1), new Float(2))));
        significantPlaceDao.insertSignificantPlace(new SignificantPlace("Kauppa", "Kauppakatu", new Coordinate(new Float(1), new Float(2))));
        assertTrue(significantPlaceDao.getAll().size() == 3);
        significantPlaceDao.deleteAllData();
        assertTrue(significantPlaceDao.getAll().isEmpty());
    }

    @Test
    public void deleteSignificantPlaceByAddress() {
        significantPlaceDao.insertSignificantPlace(new SignificantPlace("Koulu", "Kumpula", new Coordinate(new Float(1), new Float(2))));
        significantPlaceDao.insertSignificantPlace(new SignificantPlace("Toimisto", "Töölö", new Coordinate(new Float(1), new Float(2))));
        assertTrue(significantPlaceDao.getAll().size() == 2);
        significantPlaceDao.deleteSignificantPlaceByAddress("Töölö");
        assertTrue(significantPlaceDao.getAll().size() == 1);
        assertEquals("Kumpula", significantPlaceDao.getAll().get(0).getAddress());
    }
}

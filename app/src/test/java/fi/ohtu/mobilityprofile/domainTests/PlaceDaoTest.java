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
import fi.ohtu.mobilityprofile.data.PlaceDao;
import fi.ohtu.mobilityprofile.data.SignificantPlaceDao;
import fi.ohtu.mobilityprofile.domain.Place;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifestTest.xml", constants = BuildConfig.class, sdk = 21)
public class PlaceDaoTest {
    private PlaceDao placeDao;

    @Before
    public void setUp() {
        this.placeDao = new PlaceDao();
        Robolectric.setupActivity(MainActivityStub.class);
    }

  /*  @Test
    public void testInsertAndFindLatest() {
        placeDao.insert(new Place(1234, "Kumpula"));
        assertEquals("Kumpula", placeDao.getLatest().getOriginalLocation());
        placeDao.insert(new Place(1300, "Herttoniemi"));
        assertEquals("Herttoniemi", placeDao.getLatest().getOriginalLocation());
        placeDao.insert(new Place(1100, "Lammassaari"));
        assertEquals("Herttoniemi", placeDao.getLatest().getOriginalLocation());
    }
    @Test
    public void testInsertAndFindByLocation() {
        placeDao.insert(new Place(2345, "Helsinki"));
        List<Place> places = placeDao.getVisitsByLocation("Helsinki");
        assertEquals(1, places.size());
        assertEquals("Helsinki", places.get(0).getOriginalLocation());
    }
    @Test
    public void testMultipleInsertAndFindByLocation() {
        placeDao.insert(new Place(123, "Kumpula"));
        placeDao.insert(new Place(234, "Kumpula"));
        placeDao.insert(new Place(345, "Kalasatama"));
        placeDao.insert(new Place(456, "Tikkurila"));
        placeDao.insert(new Place(987, "Kumpula"));
        placeDao.insert(new Place(567, "Kumpulan kampus"));
        List<Place> places = placeDao.getVisitsByLocation("Kumpula");
        assertEquals(3, places.size());
        assertEquals("Kumpula", places.get(0).getOriginalLocation());
        assertEquals("Kumpula", places.get(1).getOriginalLocation());
        assertEquals("Kumpula", places.get(2).getOriginalLocation());
        List<Place> visits2 = placeDao.getVisitsByLocation("Tikkurila");
        assertEquals(1, visits2.size());
        assertEquals("Tikkurila", visits2.get(0).getOriginalLocation());
    }
    @Test
    public void testFindNothing() {
        assertTrue(placeDao.getLatest() == null);
        assertTrue(placeDao.getVisitsByLocation("Kumpula").isEmpty());
        placeDao.insert(new Place(234, "Kumpula"));
        assertTrue(placeDao.getVisitsByLocation("Herttoniemi").isEmpty());
    }
    @Test
    public void testDeleteAll() {
        placeDao.insert(new Place(123, "Kumpula"));
        placeDao.insert(new Place(234, "Kumpula"));
        assertEquals(2, placeDao.getVisitsByLocation("Kumpula").size());
        fi.ohtu.mobilityprofile.data.PlaceDao.deleteAll();
        assertEquals(0, placeDao.getVisitsByLocation("Kumpula").size());
    }*/
}
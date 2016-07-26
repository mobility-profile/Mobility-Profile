package fi.ohtu.mobilityprofile;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import fi.ohtu.mobilityprofile.data.SignificantPlaceDao;
import fi.ohtu.mobilityprofile.domain.Place;
import fi.ohtu.mobilityprofile.data.PlaceDao;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifestTest.xml", constants = BuildConfig.class, sdk = 21)
public class PlaceDaoTest {
    private PlaceDao placeDao;

    @Before
    public void setUp() {
        this.placeDao = new PlaceDao(new SignificantPlaceDao());
        Robolectric.setupActivity(MainActivityStub.class);
    }

    @Test
    public void testInsertAndFindLatest() {
        placeDao.insertPlace(new Place(1234, "Kumpula"));

        assertEquals("Kumpula", placeDao.getLatestVisit().getOriginalLocation());

        placeDao.insertPlace(new Place(1300, "Herttoniemi"));

        assertEquals("Herttoniemi", placeDao.getLatestVisit().getOriginalLocation());

        placeDao.insertPlace(new Place(1100, "Lammassaari"));

        assertEquals("Herttoniemi", placeDao.getLatestVisit().getOriginalLocation());
    }

    @Test
    public void testInsertAndFindByLocation() {
        placeDao.insertPlace(new Place(2345, "Helsinki"));

        List<Place> places = placeDao.getVisitsByLocation("Helsinki");

        assertEquals(1, places.size());
        assertEquals("Helsinki", places.get(0).getOriginalLocation());
    }

    @Test
    public void testMultipleInsertAndFindByLocation() {
        placeDao.insertPlace(new Place(123, "Kumpula"));
        placeDao.insertPlace(new Place(234, "Kumpula"));
        placeDao.insertPlace(new Place(345, "Kalasatama"));
        placeDao.insertPlace(new Place(456, "Tikkurila"));
        placeDao.insertPlace(new Place(987, "Kumpula"));
        placeDao.insertPlace(new Place(567, "Kumpulan kampus"));

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
        assertTrue(placeDao.getLatestVisit() == null);
        assertTrue(placeDao.getVisitsByLocation("Kumpula").isEmpty());

        placeDao.insertPlace(new Place(234, "Kumpula"));

        assertTrue(placeDao.getVisitsByLocation("Herttoniemi").isEmpty());
    }

    @Test
    public void testDeleteAll() {
        placeDao.insertPlace(new Place(123, "Kumpula"));
        placeDao.insertPlace(new Place(234, "Kumpula"));

        assertEquals(2, placeDao.getVisitsByLocation("Kumpula").size());

        PlaceDao.deleteAllData();

        assertEquals(0, placeDao.getVisitsByLocation("Kumpula").size());
    }
}

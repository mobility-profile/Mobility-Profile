package fi.ohtu.mobilityprofile;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import fi.ohtu.mobilityprofile.data.UserLocationDao;
import fi.ohtu.mobilityprofile.domain.Place;
import fi.ohtu.mobilityprofile.data.VisitDao;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifestTest.xml", constants = BuildConfig.class, sdk = 21)
public class PlaceDaoTest {
    private VisitDao visitDao;

    @Before
    public void setUp() {
        this.visitDao = new VisitDao(new UserLocationDao());
        Robolectric.setupActivity(MainActivityStub.class);
    }

    @Test
    public void testInsertAndFindLatest() {
        visitDao.insertVisit(new Place(1234, "Kumpula"));

        assertEquals("Kumpula", visitDao.getLatestVisit().getOriginalLocation());

        visitDao.insertVisit(new Place(1300, "Herttoniemi"));

        assertEquals("Herttoniemi", visitDao.getLatestVisit().getOriginalLocation());

        visitDao.insertVisit(new Place(1100, "Lammassaari"));

        assertEquals("Herttoniemi", visitDao.getLatestVisit().getOriginalLocation());
    }

    @Test
    public void testInsertAndFindByLocation() {
        visitDao.insertVisit(new Place(2345, "Helsinki"));

        List<Place> places = visitDao.getVisitsByLocation("Helsinki");

        assertEquals(1, places.size());
        assertEquals("Helsinki", places.get(0).getOriginalLocation());
    }

    @Test
    public void testMultipleInsertAndFindByLocation() {
        visitDao.insertVisit(new Place(123, "Kumpula"));
        visitDao.insertVisit(new Place(234, "Kumpula"));
        visitDao.insertVisit(new Place(345, "Kalasatama"));
        visitDao.insertVisit(new Place(456, "Tikkurila"));
        visitDao.insertVisit(new Place(987, "Kumpula"));
        visitDao.insertVisit(new Place(567, "Kumpulan kampus"));

        List<Place> places = visitDao.getVisitsByLocation("Kumpula");

        assertEquals(3, places.size());
        assertEquals("Kumpula", places.get(0).getOriginalLocation());
        assertEquals("Kumpula", places.get(1).getOriginalLocation());
        assertEquals("Kumpula", places.get(2).getOriginalLocation());

        List<Place> visits2 = visitDao.getVisitsByLocation("Tikkurila");

        assertEquals(1, visits2.size());
        assertEquals("Tikkurila", visits2.get(0).getOriginalLocation());
    }

    @Test
    public void testFindNothing() {
        assertTrue(visitDao.getLatestVisit() == null);
        assertTrue(visitDao.getVisitsByLocation("Kumpula").isEmpty());

        visitDao.insertVisit(new Place(234, "Kumpula"));

        assertTrue(visitDao.getVisitsByLocation("Herttoniemi").isEmpty());
    }

    @Test
    public void testDeleteAll() {
        visitDao.insertVisit(new Place(123, "Kumpula"));
        visitDao.insertVisit(new Place(234, "Kumpula"));

        assertEquals(2, visitDao.getVisitsByLocation("Kumpula").size());

        VisitDao.deleteAllData();

        assertEquals(0, visitDao.getVisitsByLocation("Kumpula").size());
    }
}

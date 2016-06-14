package fi.ohtu.mobilityprofile;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import fi.ohtu.mobilityprofile.data.Visit;
import fi.ohtu.mobilityprofile.data.VisitDao;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml", constants = BuildConfig.class, sdk = 21)
public class VisitDaoTest {
    private VisitDao visitDao;

    @Before
    public void setUp() {
        this.visitDao = new VisitDao();
        Robolectric.setupActivity(MainActivityStub.class);
    }

    @Test
    public void testInsertAndFindLatest() {
        visitDao.insertVisit(new Visit(1234, "Kumpula", 1));

        assertEquals("Kumpula", visitDao.getLatestVisit().getLocation());

        visitDao.insertVisit(new Visit(1300, "Herttoniemi", 1));

        assertEquals("Herttoniemi", visitDao.getLatestVisit().getLocation());

        visitDao.insertVisit(new Visit(1100, "Lammassaari", 1));

        assertEquals("Herttoniemi", visitDao.getLatestVisit().getLocation());
    }

    @Test
    public void testInsertAndFindByLocation() {
        visitDao.insertVisit(new Visit(2345, "Helsinki", 1));

        List<Visit> visits = visitDao.getVisitsByLocation("Helsinki");

        assertEquals(1, visits.size());
        assertEquals("Helsinki", visits.get(0).getLocation());
    }

    @Test
    public void testMultipleInsertAndFindByLocation() {
        visitDao.insertVisit(new Visit(123, "Kumpula", 1));
        visitDao.insertVisit(new Visit(234, "Kumpula", 1));
        visitDao.insertVisit(new Visit(345, "Kalasatama", 1));
        visitDao.insertVisit(new Visit(456, "Tikkurila", 1));
        visitDao.insertVisit(new Visit(987, "Kumpula", 1));
        visitDao.insertVisit(new Visit(567, "Kumpulan kampus", 1));

        List<Visit> visits = visitDao.getVisitsByLocation("Kumpula");

        assertEquals(3, visits.size());
        assertEquals("Kumpula", visits.get(0).getLocation());
        assertEquals("Kumpula", visits.get(1).getLocation());
        assertEquals("Kumpula", visits.get(2).getLocation());

        List<Visit> visits2 = visitDao.getVisitsByLocation("Tikkurila");

        assertEquals(1, visits2.size());
        assertEquals("Tikkurila", visits2.get(0).getLocation());
    }

    @Test
    public void testFindNothing() {
        assertTrue(visitDao.getLatestVisit() == null);
        assertTrue(visitDao.getVisitsByLocation("Kumpula").isEmpty());

        visitDao.insertVisit(new Visit(234, "Kumpula", 1));

        assertTrue(visitDao.getVisitsByLocation("Herttoniemi").isEmpty());
    }

    @Test
    public void testVisitTypes() {
        visitDao.insertVisit(new Visit(123, "Kumpula", Visit.USER_SEARCH));
        visitDao.insertVisit(new Visit(123, "Kumpula", Visit.GPS_TRACKED));

        assertEquals(2, visitDao.getVisitsByLocation("Kumpula").size());
        assertEquals(1, visitDao.getVisitsByLocation("Kumpula", Visit.USER_SEARCH).size());
        assertEquals(1, visitDao.getVisitsByLocation("Kumpula", Visit.GPS_TRACKED).size());
    }

    @Test
    public void testVisitTypes2() {
        visitDao.insertVisit(new Visit(123, "Kumpula", Visit.GPS_TRACKED));
        visitDao.insertVisit(new Visit(234, "Herttoniemi", Visit.USER_SEARCH));

        assertEquals("Kumpula", visitDao.getLatestVisit(Visit.GPS_TRACKED).getLocation());
        assertEquals("Herttoniemi", visitDao.getLatestVisit(Visit.USER_SEARCH).getLocation());
        assertEquals("Herttoniemi", visitDao.getLatestVisit().getLocation());
    }
}

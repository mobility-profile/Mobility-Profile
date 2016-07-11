package fi.ohtu.mobilityprofile;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import fi.ohtu.mobilityprofile.data.UserLocationDao;
import fi.ohtu.mobilityprofile.data.Visit;
import fi.ohtu.mobilityprofile.data.VisitDao;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifestTest.xml", constants = BuildConfig.class, sdk = 21)
public class VisitDaoTest {
    private VisitDao visitDao;

    @Before
    public void setUp() {
        this.visitDao = new VisitDao(new UserLocationDao());
        Robolectric.setupActivity(MainActivityStub.class);
    }

    @Test
    public void testInsertAndFindLatest() {
        visitDao.insertVisit(new Visit(1234, "Kumpula"));

        assertEquals("Kumpula", visitDao.getLatestVisit().getOriginalLocation());

        visitDao.insertVisit(new Visit(1300, "Herttoniemi"));

        assertEquals("Herttoniemi", visitDao.getLatestVisit().getOriginalLocation());

        visitDao.insertVisit(new Visit(1100, "Lammassaari"));

        assertEquals("Herttoniemi", visitDao.getLatestVisit().getOriginalLocation());
    }

    @Test
    public void testInsertAndFindByLocation() {
        visitDao.insertVisit(new Visit(2345, "Helsinki"));

        List<Visit> visits = visitDao.getVisitsByLocation("Helsinki");

        assertEquals(1, visits.size());
        assertEquals("Helsinki", visits.get(0).getOriginalLocation());
    }

    @Test
    public void testMultipleInsertAndFindByLocation() {
        visitDao.insertVisit(new Visit(123, "Kumpula"));
        visitDao.insertVisit(new Visit(234, "Kumpula"));
        visitDao.insertVisit(new Visit(345, "Kalasatama"));
        visitDao.insertVisit(new Visit(456, "Tikkurila"));
        visitDao.insertVisit(new Visit(987, "Kumpula"));
        visitDao.insertVisit(new Visit(567, "Kumpulan kampus"));

        List<Visit> visits = visitDao.getVisitsByLocation("Kumpula");

        assertEquals(3, visits.size());
        assertEquals("Kumpula", visits.get(0).getOriginalLocation());
        assertEquals("Kumpula", visits.get(1).getOriginalLocation());
        assertEquals("Kumpula", visits.get(2).getOriginalLocation());

        List<Visit> visits2 = visitDao.getVisitsByLocation("Tikkurila");

        assertEquals(1, visits2.size());
        assertEquals("Tikkurila", visits2.get(0).getOriginalLocation());
    }

    @Test
    public void testFindNothing() {
        assertTrue(visitDao.getLatestVisit() == null);
        assertTrue(visitDao.getVisitsByLocation("Kumpula").isEmpty());

        visitDao.insertVisit(new Visit(234, "Kumpula"));

        assertTrue(visitDao.getVisitsByLocation("Herttoniemi").isEmpty());
    }

    @Test
    public void testDeleteAll() {
        visitDao.insertVisit(new Visit(123, "Kumpula"));
        visitDao.insertVisit(new Visit(234, "Kumpula"));

        assertEquals(2, visitDao.getVisitsByLocation("Kumpula").size());

        VisitDao.deleteAllData();

        assertEquals(0, visitDao.getVisitsByLocation("Kumpula").size());
    }
}

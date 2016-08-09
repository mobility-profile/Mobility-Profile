package fi.ohtu.mobilityprofile.domainTests;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import fi.ohtu.mobilityprofile.BuildConfig;
import fi.ohtu.mobilityprofile.MainActivityStub;
import fi.ohtu.mobilityprofile.data.GpsPointDao;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifestTest.xml", constants = BuildConfig.class, sdk = 21)
public class GpsPointDaoTest {
    private static fi.ohtu.mobilityprofile.data.GpsPointDao GpsPointDao;

    @Before
    public void setUp() {
        this.GpsPointDao = new GpsPointDao();
        Robolectric.setupActivity(MainActivityStub.class);
    }

  /*  @Test
    public void testInsertAndFindLatest() {
        GpsPointDao.insert(new GpsPoint(1234, "Kumpula"));
        assertEquals("Kumpula", GpsPointDao.getLatest().getOriginalLocation());
        GpsPointDao.insert(new GpsPoint(1300, "Herttoniemi"));
        assertEquals("Herttoniemi", GpsPointDao.getLatest().getOriginalLocation());
        GpsPointDao.insert(new GpsPoint(1100, "Lammassaari"));
        assertEquals("Herttoniemi", GpsPointDao.getLatest().getOriginalLocation());
    }
    @Test
    public void testInsertAndFindByLocation() {
        GpsPointDao.insert(new GpsPoint(2345, "Helsinki"));
        List<GpsPoint> places = GpsPointDao.getVisitsByLocation("Helsinki");
        assertEquals(1, places.size());
        assertEquals("Helsinki", places.get(0).getOriginalLocation());
    }
    @Test
    public void testMultipleInsertAndFindByLocation() {
        GpsPointDao.insert(new GpsPoint(123, "Kumpula"));
        GpsPointDao.insert(new GpsPoint(234, "Kumpula"));
        GpsPointDao.insert(new GpsPoint(345, "Kalasatama"));
        GpsPointDao.insert(new GpsPoint(456, "Tikkurila"));
        GpsPointDao.insert(new GpsPoint(987, "Kumpula"));
        GpsPointDao.insert(new GpsPoint(567, "Kumpulan kampus"));
        List<GpsPoint> places = GpsPointDao.getVisitsByLocation("Kumpula");
        assertEquals(3, places.size());
        assertEquals("Kumpula", places.get(0).getOriginalLocation());
        assertEquals("Kumpula", places.get(1).getOriginalLocation());
        assertEquals("Kumpula", places.get(2).getOriginalLocation());
        List<GpsPoint> visits2 = GpsPointDao.getVisitsByLocation("Tikkurila");
        assertEquals(1, visits2.size());
        assertEquals("Tikkurila", visits2.get(0).getOriginalLocation());
    }
    @Test
    public void testFindNothing() {
        assertTrue(GpsPointDao.getLatest() == null);
        assertTrue(GpsPointDao.getVisitsByLocation("Kumpula").isEmpty());
        GpsPointDao.insert(new GpsPoint(234, "Kumpula"));
        assertTrue(GpsPointDao.getVisitsByLocation("Herttoniemi").isEmpty());
    }
    @Test
    public void testDeleteAll() {
        GpsPointDao.insert(new GpsPoint(123, "Kumpula"));
        GpsPointDao.insert(new GpsPoint(234, "Kumpula"));
        assertEquals(2, GpsPointDao.getVisitsByLocation("Kumpula").size());
        fi.ohtu.mobilityprofile.data.GpsPointDao.deleteAll();
        assertEquals(0, GpsPointDao.getVisitsByLocation("Kumpula").size());
    }*/
}

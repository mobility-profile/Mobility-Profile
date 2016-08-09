package fi.ohtu.mobilityprofile.domainTests;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import fi.ohtu.mobilityprofile.BuildConfig;
import fi.ohtu.mobilityprofile.MainActivityStub;
import fi.ohtu.mobilityprofile.data.GPSPointDao;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifestTest.xml", constants = BuildConfig.class, sdk = 21)
public class GpsPointDaoTest {
    private static GPSPointDao GpsPointDao;

    @Before
    public void setUp() {
        this.GpsPointDao = new GPSPointDao();
        Robolectric.setupActivity(MainActivityStub.class);
    }

  /*  @Test
    public void testInsertAndFindLatest() {
        GPSPointDao.insert(new GPSPoint(1234, "Kumpula"));
        assertEquals("Kumpula", GPSPointDao.getLatest().getOriginalLocation());
        GPSPointDao.insert(new GPSPoint(1300, "Herttoniemi"));
        assertEquals("Herttoniemi", GPSPointDao.getLatest().getOriginalLocation());
        GPSPointDao.insert(new GPSPoint(1100, "Lammassaari"));
        assertEquals("Herttoniemi", GPSPointDao.getLatest().getOriginalLocation());
    }
    @Test
    public void testInsertAndFindByLocation() {
        GPSPointDao.insert(new GPSPoint(2345, "Helsinki"));
        List<GPSPoint> places = GPSPointDao.getVisitsByLocation("Helsinki");
        assertEquals(1, places.size());
        assertEquals("Helsinki", places.get(0).getOriginalLocation());
    }
    @Test
    public void testMultipleInsertAndFindByLocation() {
        GPSPointDao.insert(new GPSPoint(123, "Kumpula"));
        GPSPointDao.insert(new GPSPoint(234, "Kumpula"));
        GPSPointDao.insert(new GPSPoint(345, "Kalasatama"));
        GPSPointDao.insert(new GPSPoint(456, "Tikkurila"));
        GPSPointDao.insert(new GPSPoint(987, "Kumpula"));
        GPSPointDao.insert(new GPSPoint(567, "Kumpulan kampus"));
        List<GPSPoint> places = GPSPointDao.getVisitsByLocation("Kumpula");
        assertEquals(3, places.size());
        assertEquals("Kumpula", places.get(0).getOriginalLocation());
        assertEquals("Kumpula", places.get(1).getOriginalLocation());
        assertEquals("Kumpula", places.get(2).getOriginalLocation());
        List<GPSPoint> visits2 = GPSPointDao.getVisitsByLocation("Tikkurila");
        assertEquals(1, visits2.size());
        assertEquals("Tikkurila", visits2.get(0).getOriginalLocation());
    }
    @Test
    public void testFindNothing() {
        assertTrue(GPSPointDao.getLatest() == null);
        assertTrue(GPSPointDao.getVisitsByLocation("Kumpula").isEmpty());
        GPSPointDao.insert(new GPSPoint(234, "Kumpula"));
        assertTrue(GPSPointDao.getVisitsByLocation("Herttoniemi").isEmpty());
    }
    @Test
    public void testDeleteAll() {
        GPSPointDao.insert(new GPSPoint(123, "Kumpula"));
        GPSPointDao.insert(new GPSPoint(234, "Kumpula"));
        assertEquals(2, GPSPointDao.getVisitsByLocation("Kumpula").size());
        fi.ohtu.mobilityprofile.data.GPSPointDao.deleteAll();
        assertEquals(0, GPSPointDao.getVisitsByLocation("Kumpula").size());
    }*/
}